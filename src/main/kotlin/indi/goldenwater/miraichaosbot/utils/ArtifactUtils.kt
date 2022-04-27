package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.api.interfaces.type.genshin.*
import indi.goldenwater.miraichaosbot.data.genshin.ArtifactLevelUpInfo
import indi.goldenwater.miraichaosbot.data.genshin.CharacterArtifactInfos
import indi.goldenwater.miraichaosbot.defaultFont
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

/**
 * 对于合适的属性的加成最高值在最终分数的占比 0~1
 */
const val attributeApplicabilityScoreInTotalScore = 0.25

/**
 * 满分分数
 */
const val maxScore = 100

/**
 * 计算属性适用性前的最高分数
 */
const val maxScoreBeforeCalcAttributeApplicability = maxScore * (1 - attributeApplicabilityScoreInTotalScore)

const val maxScorePerAttribute = maxScoreBeforeCalcAttributeApplicability * 2 / 3

val valueToScoreScaler: ArtifactAttribute = "".run {
    val result = ArtifactAttribute()

    ArtifactAttribute::class.memberProperties.filterIsInstance<KMutableProperty<*>>().forEach {
        val maxPerUpgrade = it.getter.call(ArtifactLevelUpInfo.data[3])
        if (maxPerUpgrade !is Double) return@forEach
        val maxValue = maxPerUpgrade * 6
        it.setter.call(result, maxScorePerAttribute / maxValue)
    }

    return@run result
}

fun ArtifactAttribute.calcArtifactScore(): ArtifactScore {
    val result = ArtifactScore()

    var sumResult = 0.0
    ArtifactAttribute::class.memberProperties.filterIsInstance<KMutableProperty<*>>().forEach {
        val value = it.getter.call(this)
        if (value !is Double) return@forEach
        val scaler = it.getter.call(valueToScoreScaler)
        if (scaler !is Double) return@forEach

        val score = value * scaler
        it.setter.call(result.data, score)
        sumResult += score
    }
    result.sum = sumResult
    result.sumInPercentage = sumResult / maxScoreBeforeCalcAttributeApplicability * 100

    return result
}

fun ArtifactScore.getScoreOfAllCharacterBuild(): CharacterBuildNameAndScores {
    val result: MutableMap<CharacterBuildName, ArtifactScore> = mutableMapOf()

    CharacterArtifactInfos.data.forEach {
        result["${it.characterName} - ${it.buildName}"] = this.multiplyWithCharacterInfo(it)
    }

    return result
}

fun CharacterBuildNameAndScores.toSortedString(): String {
    val stringBuilder = StringBuilder("这个副属性与角色发展方向的适配程度\n")

    this.map { CharacterBuildNameAndScore(it.key, it.value) }
        .sortedByDescending { it.score.sum }
        .forEach { stringBuilder.append("${it.score.sumInPercentage.roundTo(2)}/100: ${it.characterBuildName}\n") }

    return stringBuilder.toString().trim()
}

fun CharacterBuildNameAndScores.toImage(): ExternalResource {
    return this.toSortedString().toImageExternalResource()
}

fun String.toImage(): BufferedImage {
    val font = defaultFont?.deriveFont(48f)
    val lineSplit = this.split("\n")
    if (lineSplit.isEmpty()) return BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB)

    // 测量大小
    var img = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    var g2d = img.createGraphics()
    g2d.font = font
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val fm = g2d.fontMetrics
    val width = fm.stringWidth(lineSplit.maxByOrNull { fm.stringWidth(it) }!!)
    val height = fm.height
    val totalHeight = height * lineSplit.size
    val ascent = fm.ascent
    g2d.dispose()

    // 绘制
    img = BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB)
    g2d = img.createGraphics()
    g2d.font = font
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.background = Color.BLACK
    g2d.color = Color.WHITE
    lineSplit.forEachIndexed { i, v ->
        g2d.drawString(v, 0, i * height + ascent)
    }
    g2d.dispose()
    return img
}

fun String.toImageExternalResource(): ExternalResource {
    val outputStream = ByteArrayOutputStream()

    ImageIO.write(this.toImage(), "png", outputStream)

    val inputStream = ByteArrayInputStream(outputStream.toByteArray())

    val result = inputStream.toExternalResource("png")

    outputStream.close()
    inputStream.close()

    return result
}

fun String.parseToArtifactAttribute(): ArtifactAttribute {
    val result = ArtifactAttribute()

    ArtifactAttribute::class.memberProperties.filterIsInstance<KMutableProperty<*>>().forEach {
        val value = Regex("""${it.name}-([\d.]+)""")
            .find(this)
            ?.groupValues
            ?.getOrNull(1) ?: Regex("""${ArtifactAttribute().attNameToLocateStr(it.name)}-([\d.]+)""")
            .find(this)
            ?.groupValues
            ?.getOrNull(1) ?: return@forEach
        val doubleValue = value.toDoubleOrNull() ?: return@forEach

        it.setter.call(result, doubleValue)
    }

    return result
}

suspend fun User.sendArtifactSortedScoresImage(artifactInfo: String) {
    val score = artifactInfo.parseToArtifactAttribute().calcArtifactScore()
    val scoreOfAllCharacterBuild = score.getScoreOfAllCharacterBuild()

    val picFile = scoreOfAllCharacterBuild.toImage().toAutoCloseable()
    val image: Image = if (this is Member) {
        this.group.uploadImage(picFile)
    } else {
        this.uploadImage(picFile)
    }

    this.sendMessageTo(
        PlainText(
            """
                |分数: ${score.sumInPercentage.roundTo(2)}/100
                |
                """.trimMargin()
        ).plus(image)
    )
}
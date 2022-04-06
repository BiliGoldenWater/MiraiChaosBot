package indi.goldenwater.miraichaosbot.api.interfaces.type.genshin

import indi.goldenwater.miraichaosbot.utils.maxScore
import indi.goldenwater.miraichaosbot.utils.maxScoreBeforeCalcAttributeApplicability
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

data class ArtifactScore(
    val data: ArtifactAttribute = ArtifactAttribute(),

    var sum: Double = 0.0,
    var sumInPercentage: Double = 0.0,
) {
    fun multiplyWithCharacterInfo(characterArtifactInfo: CharacterArtifactInfo): ArtifactScore {
        @Suppress("UnnecessaryVariable") val cai = characterArtifactInfo
        val maxMultiplier = (maxScore / maxScoreBeforeCalcAttributeApplicability) - 1

        val result = ArtifactScore()

        var sumResult = 0.0
        ArtifactAttribute::class.memberProperties.filterIsInstance<KMutableProperty<*>>().forEach {
            val caiV = it.getter.call(cai.data)
            if (caiV !is Double) return@forEach

            val multiplier: Double = if (caiV > 0) maxMultiplier * caiV else 0.0

            val score = it.getter.call(this.data)
            if (score !is Double) return@forEach

            val multipliedScore = score * (1.0 + multiplier)

            it.setter.call(result.data, multipliedScore)
            sumResult += multipliedScore
        }
        result.sum = sumResult
        result.sumInPercentage = sumResult / maxScore * 100

        return result
    }
}
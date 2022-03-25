package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.MiraiChaosBot
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.ResultInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.bilibili.BiliBiliResponse
import indi.goldenwater.miraichaosbot.api.interfaces.type.bilibili.videoinfo.BiliBiliVideoInfo
import indi.goldenwater.miraichaosbot.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message

@OptIn(ExperimentalSerializationApi::class)
suspend fun getBiliBiliVideoById(id: String): ResultInfo<BiliBiliVideoInfo> {
    val result = ResultInfo<BiliBiliVideoInfo>()
    var response: String? = null

    if (id.startsWith("av", ignoreCase = true)) {
        val avidRegex = Regex("""av(\d+)""", RegexOption.IGNORE_CASE)
        response = httpGetText(
            "https://api.bilibili.com/x/web-interface/view?aid=${
                id.replace(avidRegex, "$1")
            }"
        )
    } else if (id.startsWith("bv", ignoreCase = true)) {
        response = httpGetText("https://api.bilibili.com/x/web-interface/view?bvid=$id")
    }

    val biliBiliResponse: BiliBiliResponse<BiliBiliVideoInfo> = json.decodeFromString(response ?: return result)

    if (biliBiliResponse.code == 0) {
        result.status = ResultInfo.Status.Success
        result.result = biliBiliResponse.data
    } else {
        MiraiChaosBot.logger.warning(response)
    }

    return result
}

suspend fun parseBiliBiliVideo(messageInfo: DMessageInfo, msg: String) {
    val linkOrIDRegex = Regex(
        """(((http|https)://)?(www\.)?(b23|bilibili)\.(com|tv)/(video/)?)?(BV[a-zA-Z\d]{10}|av\d+)""",
        setOf(RegexOption.IGNORE_CASE)
    )
    val idRegex = Regex("""BV[a-zA-Z\d]{10}|av\d+""", setOf(RegexOption.IGNORE_CASE))
    val shortLinkRegex = Regex(
        """(((http|https)://)?b23\.tv/)[a-zA-Z\d]{7}([&?]([a-zA-Z\-_\d%^=]*)=([^& ]*))*${'$'}""",
        setOf(RegexOption.IGNORE_CASE)
    )

    if (msg.matches(linkOrIDRegex)) {
        val id = idRegex.find(msg)?.value ?: return

        getBiliBiliVideoById(id).result?.let {
            messageInfo.sender.sendSimpleBiliBiliVideoInfo(it)
        }
    } else if (shortLinkRegex.find(msg) != null) {
        val link = shortLinkRegex.find(msg)?.value ?: return
        parseBiliBiliVideo(
            messageInfo,
            linkOrIDRegex.find(httpGetRedirectTarget(link) ?: return)
                ?.value ?: return
        )
    }
}

private suspend fun User.sendSimpleBiliBiliVideoInfo(data: BiliBiliVideoInfo) {
    val picFile = httpGetFile(data.pic).img
    val image: Image = if (this is Member) {
        this.group.uploadImage(picFile)
    } else {
        this.uploadImage(picFile)
    }
    withContext(Dispatchers.IO) {
        picFile.close()
    }


    val message: Message = image.plus(
        """${"\n"}
                |${data.owner.name}
                |${data.title.cutLength(18)}
                |
                |${data.desc.cutLength(40)}
                |
                |b23.tv/${data.bvid}
            """.trimMargin()
    )

    this.sendMessageWithoutAt(message)
}
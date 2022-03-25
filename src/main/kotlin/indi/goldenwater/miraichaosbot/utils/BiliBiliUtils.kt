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
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message

@OptIn(ExperimentalSerializationApi::class)
suspend fun getBiliBiliVideoById(id: String): ResultInfo<BiliBiliVideoInfo> {
    val result = ResultInfo<BiliBiliVideoInfo>()
    var response: String? = null

    if (id.startsWith("av", ignoreCase = true)) {
        response = httpGet(
            "https://api.bilibili.com/x/web-interface/view?aid=${
                id.replace(
                    Regex(
                        """av(\d+)""",
                        RegexOption.IGNORE_CASE
                    ),
                    "$1"
                )
            }"
        )
    } else if (id.startsWith("bv", ignoreCase = true)) {
        response = httpGet("https://api.bilibili.com/x/web-interface/view?bvid=$id")
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
    if (msg.matches(
            Regex(
                """(((http|https)://)?(www\.)?(b23|bilibili)\.(com|tv)/(video/)?)?(BV[a-zA-Z\d]+|av\d+)""",
                setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
            )
        )
    ) {
        val id = Regex("""(BV[a-zA-Z\d]+|av\d+)""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
            .find(msg)
            ?.groups
            ?.get(0)
            ?.value ?: return


        val result = getBiliBiliVideoById(id)
        if (result.status == ResultInfo.Status.Success) {
            val data = result.result ?: return
            val sender = messageInfo.sender

            val picFile = httpGetFile(data.pic).img
            val image: Image = if (sender is Member) {
                sender.group.uploadImage(picFile)
            } else {
                sender.uploadImage(picFile)
            }
            withContext(Dispatchers.IO) {
                picFile.close()
            }


            val message: Message = image.plus(
                """${"\n"}
                |UP: ${data.owner.name}
                |标题: ${data.title.cutLength(16)}
                |简介: ${data.desc.cutLength(40)}
                |
                |链接: b23.tv/${data.bvid}
            """.trimMargin()
            )

            sender.sendMessageWithoutAt(message)
        }
    }
}
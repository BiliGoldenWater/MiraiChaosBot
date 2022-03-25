package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.MiraiChaosBot
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.ResultInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic.DSearchResult
import indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic.DSong
import indi.goldenwater.miraichaosbot.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import java.io.IOException

fun getProperty(detailString: String, key: String): String {
    return (
            Regex("property=\"$key\" content=\"([^\"]*)\"")
                .find(detailString)
                ?.destructured
                ?.component1()
            )
        ?: "网易云音乐"
}

suspend fun getMusicById(id: String): ResultInfo<MusicShare> {
    val result = ResultInfo<MusicShare>()
    try {
        val musicUrl = "http://music.163.com/song/media/outer/url?id=$id"
        val detail = httpGet("https://music.163.com/song?id=$id")
        val title = getProperty(detail, "og:title")
        if (title == "网易云音乐") {
            result.status = ResultInfo.Status.FailedByUnknownMusicInfo
            return result
        }
        val artist = getProperty(detail, "og:music:artist")
        val jumpUrl = getProperty(detail, "og:url")
        val pictureUrl = getProperty(detail, "og:image")

        result.result = MusicShare(
            MusicKind.NeteaseCloudMusic,
            title,
            artist,
            jumpUrl,
            pictureUrl,
            musicUrl
        )
        result.status = ResultInfo.Status.Success

    } catch (e: IOException) {
        result.status = ResultInfo.Status.FailedByException
        e.printStackTrace()
    }
    return result
}

@OptIn(ExperimentalSerializationApi::class)
suspend fun getMusicByName(name: String): ResultInfo<MusicShare> {
    val result = ResultInfo<MusicShare>()
    try {
        val searchUrl = """
            |http://music.163.com/api/search/get/web
            |?csrf_token=hlpretag=&hlposttag=&total=true
            |&limit=${20}
            |&offset=${0}
            |&s=${name}
            |&type=${1}
            |""".trimMargin().replace("\n", "")
        val res = httpGet(searchUrl)
        val searchResult = json.decodeFromString<DSearchResult>(res)
        if (searchResult.code != 200) {
            MiraiChaosBot.logger.error("keyword: \"$name\",code: ${searchResult.code}")
            result.status = ResultInfo.Status.FailedByUnknownSearchResultCode
            return result
        }
        if ((searchResult.result?.songCount ?: 0) == 0) {
            result.status = ResultInfo.Status.FailedByEmptySearchResult
            return result
        }
        val song: DSong = searchResult.result?.songs?.get(0) ?: result.let {
            it.status = ResultInfo.Status.FailedByUnknownMusicInfo
            return it
        }
        return getMusicById(song.id.toString())
    } catch (e: Exception) {
        result.status = ResultInfo.Status.FailedByException
        e.printStackTrace()
    }
    return result
}

suspend fun parseNeteaseMusicLink(messageInfo: DMessageInfo, msg: String) {
    if (msg.matches(
            Regex(
                """(.*://)?(y\.)?music.163.com/(m/)?song([&?][^=]*=[^&]*)*""",
                setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
            )
        )
    ) {
        val id = Regex("""[&?]id=(\d+)""")
            .find(msg)
            ?.groups
            ?.get(1)
            ?.value ?: return

        val result = getMusicById(id)
        if (result.status == ResultInfo.Status.Success) {
            messageInfo.sender.sendMessageTo(result.result ?: return)
        }
    }
}
package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.api.interfaces.type.DSearchResult
import indi.goldenwater.miraichaosbot.api.interfaces.type.DSong
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import net.mamoe.mirai.utils.MiraiLogger
import java.io.IOException

var l: MiraiLogger? = null
var json: Json = Json {
    ignoreUnknownKeys = true
}

fun initLogger(logger: MiraiLogger) {
    l = logger
}

fun getProperty(detailString: String, key: String): String {
    return (
            Regex("property=\"$key\" content=\"([^\"]*)\"")
                .find(detailString)
                ?.destructured
                ?.component1()
            )
        ?: "网易云音乐"
}

suspend fun getMusicById(id: String): Result<MusicShare> {
    val result = Result<MusicShare>()
    try {
        val musicUrl = "http://music.163.com/song/media/outer/url?id=$id"
        val detail = httpGet("https://music.163.com/song?id=$id")
        val title = getProperty(detail, "og:title")
        if (title == "网易云音乐") {
            result.status = Result.Status.FailedByUnknownMusicInfo
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
        result.status = Result.Status.Success

    } catch (e: IOException) {
        result.status = Result.Status.FailedByException
        e.printStackTrace()
    }
    return result
}

@OptIn(ExperimentalSerializationApi::class)
suspend fun getMusicByName(name: String): Result<MusicShare> {
    val result = Result<MusicShare>()
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
            l?.error("keyword: \"$name\",code: ${searchResult.code}")
            result.status = Result.Status.FailedByUnknownSearchResultCode
            return result
        }
        if ((searchResult.result?.songCount ?: 0) == 0) {
            result.status = Result.Status.FailedByEmptySearchResult
            return result
        }
        val song: DSong = searchResult.result?.songs?.get(0) ?: result.let {
            it.status = Result.Status.FailedByUnknownMusicInfo
            return it
        }
        return getMusicById(song.id.toString())
    } catch (e: Exception) {
        result.status = Result.Status.FailedByException
        e.printStackTrace()
    }
    return result
}

class Result<T> {
    var result: T? = null
    var status: Status = Status.Unknown

    enum class Status(private val message: String) {
        Unknown("未知"),
        Success("成功"),
        FailedByUnknownSearchResultCode("搜索失败"),
        FailedByEmptySearchResult("没有搜索到相关音乐"),
        FailedByUnknownMusicInfo("无法获取音乐信息"),
        FailedByException(CommandReplyMessage.FailedByException.s())
        ;

        fun s() = toString()

        override fun toString() = message
    }
}
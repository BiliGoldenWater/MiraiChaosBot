package indi.goldenwater.miraichaosbot.api.interfaces.type

import indi.goldenwater.miraichaosbot.command.CommandReplyMessage

class ResultInfo<T> {
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
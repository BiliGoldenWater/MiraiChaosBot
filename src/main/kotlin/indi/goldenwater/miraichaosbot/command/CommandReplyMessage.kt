package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.command.CommandManager.prefix as p
import net.mamoe.mirai.message.data.PlainText

enum class CommandReplyMessage(private val message: String) {
    Help(
        """
        |${p}help 显示本消息
        |${p}song <byId|search> <id|name> 根据歌曲id或歌曲名点歌(仅限网易云)
    """.trimMargin()
    ),

    UnknownUsage("未知的用法, 请输入${p}help查询"),
    FailedByException("发生了错误"),
    FailedByUnknownReason("未知的错误")
    ;

    fun s() = toString()
    fun m() = PlainText(toString())

    override fun toString() = message
}
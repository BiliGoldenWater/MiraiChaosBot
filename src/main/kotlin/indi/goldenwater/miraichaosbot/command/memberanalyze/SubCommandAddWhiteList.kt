package indi.goldenwater.miraichaosbot.command.memberanalyze

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.config.Config
import indi.goldenwater.miraichaosbot.utils.sendMessage
import indi.goldenwater.miraichaosbot.utils.senderToMemberCheckPermission
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

class SubCommandAddWhiteList : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = senderToMemberCheckPermission(messageInfo.sender) ?: return true

        if (args.isEmpty()) {
            sendMessage(sender, CommandReplyMessage.UnknownUsage.s())
            return true
        }

        var message: Message = PlainText("")

        for (arg in args) {
            val qqId: Long = arg.toLongOrNull() ?: -1

            if (qqId <= 0) {
                message = message.plus(CommandReplyMessage.AddFailedBy.s().format(arg, "不是一个正确的qq号") + "\n")
                continue
            }

            Config.nonActiveWhiteList.add(qqId)

            message = message.plus("$arg ${CommandReplyMessage.Added.s()}")
        }

        sendMessage(sender, message)

        return true
    }
}
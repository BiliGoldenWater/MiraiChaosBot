package indi.goldenwater.miraichaosbot.command.song

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.ResultInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.ResultInfo.Status.Success
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.getMusicById
import indi.goldenwater.miraichaosbot.utils.sendMessageTo
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MusicShare

class SubCommandById : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender: User = messageInfo.sender
        if (args.isEmpty()) {
            sender.sendMessageTo(CommandReplyMessage.UnknownUsage.s())
            return true
        }

        val result: ResultInfo<MusicShare> = getMusicById(args[0])

        if (result.status == Success) {
            sender.sendMessageTo(result.result ?: CommandReplyMessage.FailedByUnknownReason.m())
        } else {
            sender.sendMessageTo(result.status.s())
        }

        return true
    }
}
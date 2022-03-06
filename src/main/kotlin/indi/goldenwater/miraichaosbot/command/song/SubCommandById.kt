package indi.goldenwater.miraichaosbot.command.song

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.Result
import indi.goldenwater.miraichaosbot.utils.Result.Status.*
import indi.goldenwater.miraichaosbot.utils.getMusicById
import indi.goldenwater.miraichaosbot.utils.sendMessage
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MusicShare

class SubCommandById : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender: User = messageInfo.sender
        if (args.isEmpty()) {
            sendMessage(sender, CommandReplyMessage.UnknownUsage.s())
            return true
        }

        val result: Result<MusicShare> = getMusicById(args[0]);

        if (result.status == Success) {
            sendMessage(sender, result.result ?: CommandReplyMessage.FailedByUnknownReason.m())
        } else {
            sendMessage(sender, result.status.s())
        }

        return true
    }
}
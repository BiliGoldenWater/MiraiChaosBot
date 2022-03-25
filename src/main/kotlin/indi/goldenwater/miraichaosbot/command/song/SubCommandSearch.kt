package indi.goldenwater.miraichaosbot.command.song

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.api.interfaces.type.ResultInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.getMusicByName
import indi.goldenwater.miraichaosbot.utils.sendMessageTo

class SubCommandSearch : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = messageInfo.sender

        val result = getMusicByName(args.joinToString(" "))

        when (result.status) {
            ResultInfo.Status.Success -> {
                sender.sendMessageTo(result.result ?: CommandReplyMessage.FailedByUnknownReason.m())
            }
            else -> {
                sender.sendMessageTo(result.status.s())
            }
        }

        return true
    }
}
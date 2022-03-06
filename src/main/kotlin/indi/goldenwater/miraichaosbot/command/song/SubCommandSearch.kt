package indi.goldenwater.miraichaosbot.command.song

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.Result
import indi.goldenwater.miraichaosbot.utils.getMusicByName
import indi.goldenwater.miraichaosbot.utils.sendMessage

class SubCommandSearch : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = messageInfo.sender

        val result = getMusicByName(args.joinToString(" "))

        when (result.status) {
            Result.Status.Success -> {
                sendMessage(sender, result.result ?: CommandReplyMessage.FailedByUnknownReason.m())
            }
            else -> {
                sendMessage(sender, result.status.s())
            }
        }

        return true
    }
}
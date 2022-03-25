package indi.goldenwater.miraichaosbot.command.song

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.sendMessageTo

class CommandSong : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        messageInfo.sender.sendMessageTo(CommandReplyMessage.UnknownUsage.s())
        return true
    }
}
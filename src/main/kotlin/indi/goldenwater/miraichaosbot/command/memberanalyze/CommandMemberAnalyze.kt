package indi.goldenwater.miraichaosbot.command.memberanalyze

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.utils.sendMessage

class CommandMemberAnalyze : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        sendMessage(messageInfo.sender, CommandReplyMessage.UnknownUsage.s())
        return true
    }
}
package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.sendMessageTo
import indi.goldenwater.miraichaosbot.utils.sendArtifactSortedScoresImage

class CommandArtifactScore : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = messageInfo.sender
        if (args.isEmpty()) {
            sender.sendMessageTo(CommandReplyMessage.UnknownUsage.s())
            return true
        }

        sender.sendArtifactSortedScoresImage(args[0])

        return true
    }
}
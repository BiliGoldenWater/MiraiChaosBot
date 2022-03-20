package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.sendMessage
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.contact.NormalMember

class CommandHelp : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        var helpMessage = CommandReplyMessage.Help.s()
        if (messageInfo.sender is NormalMember && messageInfo.sender.permission > MemberPermission.MEMBER) {
            helpMessage += "\n" + CommandReplyMessage.HelpAdmin.s()
        }

        sendMessage(messageInfo.sender, helpMessage)
        return true
    }
}
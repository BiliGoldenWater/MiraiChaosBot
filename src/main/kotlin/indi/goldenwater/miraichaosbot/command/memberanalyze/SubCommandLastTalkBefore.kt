package indi.goldenwater.miraichaosbot.command.memberanalyze

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.config.Config
import indi.goldenwater.miraichaosbot.utils.formatWithTime
import indi.goldenwater.miraichaosbot.utils.getTimeInSeconds
import indi.goldenwater.miraichaosbot.utils.sendMessage
import indi.goldenwater.miraichaosbot.utils.senderToMemberCheckPermission
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

class SubCommandLastTalkBefore : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = senderToMemberCheckPermission(messageInfo.sender) ?: return true

        val resultMembers: MutableSet<NormalMember> = mutableSetOf()
        val lastTalkBefore = args.let {
            if (args.isEmpty()) Int.MIN_VALUE

            getTimeInSeconds(args[0])
        }

        if (lastTalkBefore == Int.MIN_VALUE) {
            sendMessage(messageInfo.sender, CommandReplyMessage.UnknownUsage.s())
            return true
        }

        for (member in sender.group.members) {
            if (
                member.lastSpeakTimestamp + lastTalkBefore <= messageInfo.time
                && !Config.nonActiveWhiteList.contains(member.id)
            ) {
                resultMembers.add(member)
            }
        }

        var message: Message = PlainText("")

        for (member in resultMembers) {
            message =
                message.plus("距上次发言 ${formatWithTime(member, (messageInfo.time) - (member.lastSpeakTimestamp))}\n")
        }

        sendMessage(sender, message.plus("总计: ${resultMembers.size}"))

        return true
    }
}
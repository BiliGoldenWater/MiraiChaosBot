package indi.goldenwater.miraichaosbot.command.memberanalyze

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import indi.goldenwater.miraichaosbot.config.Config
import indi.goldenwater.miraichaosbot.utils.formatWithTime
import indi.goldenwater.miraichaosbot.utils.getTimeInSeconds
import indi.goldenwater.miraichaosbot.utils.sendMessageTo
import indi.goldenwater.miraichaosbot.utils.senderToMemberCheckPermission
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

class SubCommandNeverTalk : ACommandHandler() {
    private fun isNeverTalk(member: NormalMember): Boolean {
        return member.joinTimestamp == member.lastSpeakTimestamp
    }

    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender = senderToMemberCheckPermission(messageInfo.sender) ?: return true

        val resultMembers: MutableSet<NormalMember> = mutableSetOf()
        val joinBefore = args.let {
            if (args.isEmpty()) return@let 0

            getTimeInSeconds(args[0])
        }

        if (joinBefore == Int.MIN_VALUE) {
            sender.sendMessageTo(CommandReplyMessage.UnknownUsage.s())
            return true
        }

        for (member in sender.group.members) {
            if (
                isNeverTalk(member)
                && (member.joinTimestamp + joinBefore <= messageInfo.time)
                && !Config.nonActiveWhiteList.contains(member.id)
            ) {
                resultMembers.add(member)
            }
        }

        var message: Message = PlainText("")

        for (member in resultMembers) {
            message = message.plus("入群已 ${formatWithTime(member, (messageInfo.time) - (member.joinTimestamp))}\n")
        }

        sender.sendMessageTo(message.plus("总计: ${resultMembers.size}"))

        return true
    }
}
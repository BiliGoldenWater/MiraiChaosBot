package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.command.CommandReplyMessage
import net.mamoe.mirai.contact.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

fun formatWithTime(member: NormalMember, time: Int): String {
    val duration = time.seconds.toJavaDuration()
    val durationStr = "${duration.toDaysPart()} 天 " +
            "${duration.toHoursPart()} 小时 "
    return "$durationStr ${member.id} ${member.nameCardOrNick}"
}

suspend fun senderToMemberCheckPermission(sender: User): Member? {
    if (sender !is Member) {
        sendMessage(sender, CommandReplyMessage.GroupOnly.s())
        return null
    }

    if (sender.permission < MemberPermission.ADMINISTRATOR) {
        sendMessage(sender, CommandReplyMessage.NoPermission.s())
        return null
    }

    return sender
}
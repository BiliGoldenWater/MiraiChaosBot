package indi.goldenwater.miraichaosbot.utils

import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.nameCardOrNick
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

fun formatWithTime(member: NormalMember, time: Int): String {
    val duration = time.seconds.toJavaDuration()
    val durationStr = "${duration.toDaysPart()} 天 " +
            "${duration.toHoursPart()} 小时 " +
            "${duration.toMinutesPart()} 分 " +
            "${duration.toSecondsPart()} 秒"
    return "$durationStr ${member.id} ${member.nameCardOrNick}"
}
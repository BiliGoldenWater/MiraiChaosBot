package indi.goldenwater.miraichaosbot.utils

fun daysToSeconds(days: Int): Int {
    return days * 24 * 60 * 60
}

/**
 * 格式: [月:]天
 * 注: 月为30天
 * 返回 Int.MIN_VALUE 为出错
 * 不接受 [0:]0
 */
fun getTimeInSeconds(text: String): Int {
    var result: Int

    if (text.contains(":")) {
        text.split(":").let {
            if (it.size > 2) return Int.MIN_VALUE

            val months = it[0].toIntOrNull() ?: return Int.MIN_VALUE
            val days = it[1].toIntOrNull() ?: return Int.MIN_VALUE
            result = daysToSeconds(months * 30 + days)
        }
    } else {
        result = text.toIntOrNull()?.let { daysToSeconds(it) } ?: return Int.MIN_VALUE
    }

    if (result <= 0) result = Int.MIN_VALUE

    return result
}
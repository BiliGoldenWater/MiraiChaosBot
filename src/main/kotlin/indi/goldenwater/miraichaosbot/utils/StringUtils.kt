package indi.goldenwater.miraichaosbot.utils

fun String.cutLength(targetLength: Int, suffix: String = "..."): String {
    return this.take(targetLength) + (suffix.takeIf { this.length > targetLength } ?: "")
}
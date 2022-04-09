package indi.goldenwater.miraichaosbot.utils

import kotlin.math.pow
import kotlin.math.roundToLong

fun Number.roundTo(num: Int): Double {
    val scaler: Int = (10.0).pow(num.toDouble()).toInt()
    return (this.toDouble() * scaler).roundToLong() / scaler.toDouble()
}
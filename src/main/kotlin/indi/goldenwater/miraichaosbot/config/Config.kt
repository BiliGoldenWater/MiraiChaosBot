package indi.goldenwater.miraichaosbot.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("Config") {
    val nonActiveWhiteList: MutableSet<Long> by value()
}
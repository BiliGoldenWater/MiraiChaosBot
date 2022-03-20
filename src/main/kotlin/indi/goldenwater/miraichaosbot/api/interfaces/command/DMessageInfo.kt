package indi.goldenwater.miraichaosbot.api.interfaces.command

import net.mamoe.mirai.contact.User

data class DMessageInfo(
    val sender: User,
    /**
     * Second(s)
     */
    val time: Int
)

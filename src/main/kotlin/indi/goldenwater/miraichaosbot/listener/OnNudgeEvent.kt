package indi.goldenwater.miraichaosbot.listener

import indi.goldenwater.miraichaosbot.utils.sendNudge
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.NudgeEvent

object OnNudgeEvent {
    private var listener: Listener<NudgeEvent>? = null

    fun register() {
        listener = GlobalEventChannel.subscribeAlways { event ->
            if (event.from is User && event.target.id == event.from.bot.id) {
                val sender = (event.from as User)
                sender.sendNudge()
            }
        }
    }

    fun unregister() {
        listener?.complete()
    }
}
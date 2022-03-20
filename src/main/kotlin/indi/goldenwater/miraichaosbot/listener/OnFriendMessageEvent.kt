package indi.goldenwater.miraichaosbot.listener

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.FriendMessageEvent


object OnFriendMessageEvent {
    private var listener: Listener<FriendMessageEvent>? = null

    fun register() {
        listener = GlobalEventChannel.subscribeAlways { event ->
            CommandManager.processMessage(
                DMessageInfo(
                    event.sender,
                    event.time
                ),
                event.message.contentToString()
            )
        }
    }

    fun unregister() {
        listener?.complete()
    }
}
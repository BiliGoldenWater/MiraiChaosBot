package indi.goldenwater.miraichaosbot.listener

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.GroupMessageEvent


object OnGroupMessageEvent {
    private var listener: Listener<GroupMessageEvent>? = null

    fun register() {
        listener = GlobalEventChannel.subscribeAlways { event ->
            CommandManager.processMessage(
                DMessageInfo(
                    event.sender,
                ),
                event.message.contentToString()
            )
        }
    }

    fun unregister() {
        listener?.complete()
    }
}
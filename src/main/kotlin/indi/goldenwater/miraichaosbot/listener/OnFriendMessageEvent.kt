package indi.goldenwater.miraichaosbot.listener

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.parseBiliBiliVideo
import indi.goldenwater.miraichaosbot.utils.parseNeteaseMusicLink
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.FriendMessageEvent


@Suppress("DuplicatedCode")
object OnFriendMessageEvent {
    private var listener: Listener<FriendMessageEvent>? = null

    fun register() {
        listener = GlobalEventChannel.subscribeAlways { event ->
            val messageInfo = DMessageInfo(
                event.sender,
                event.time
            )
            val isCommand = CommandManager.processMessage(
                messageInfo,
                event.message.contentToString()
            )

            if (!isCommand) {
                val msg = event.message.contentToString()
                parseNeteaseMusicLink(messageInfo, msg)
                parseBiliBiliVideo(messageInfo, msg)
            }
        }
    }

    fun unregister() {
        listener?.complete()
    }
}
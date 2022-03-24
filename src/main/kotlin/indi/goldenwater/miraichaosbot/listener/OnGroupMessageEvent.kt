package indi.goldenwater.miraichaosbot.listener

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.Result.Status
import indi.goldenwater.miraichaosbot.utils.getMusicById
import indi.goldenwater.miraichaosbot.utils.sendMessage
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.GroupMessageEvent


object OnGroupMessageEvent {
    private var listener: Listener<GroupMessageEvent>? = null

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
            }
        }
    }

    fun unregister() {
        listener?.complete()
    }

    private suspend fun parseNeteaseMusicLink(messageInfo: DMessageInfo, msg: String) {
        if (msg.matches(
                Regex(
                    ".*://(y\\.)?music.163.com/(m/)?song([&?][^=]*=[^&]*)*",
                    setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
                )
            )
        ) {
            val id = Regex("[&?]id=([0-9]*)")
                .find(msg)
                ?.groups
                ?.get(1)
                ?.value ?: return

            val result = getMusicById(id)
            if (result.status == Status.Success) {
                sendMessage(messageInfo.sender, result.result ?: return)
            }
        }
    }
}
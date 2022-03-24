package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.FileResult
import indi.goldenwater.miraichaosbot.utils.getRandomImage
import indi.goldenwater.miraichaosbot.utils.sendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage

class CommandRandomAcg : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val recallDelaySeconds = 15

        val sender: User = messageInfo.sender

        val sendingMessageReceipt = sendMessage(sender, CommandReplyMessage.GettingInfo.s())

        val result: FileResult = getRandomImage()
        val image: Image = result.img.uploadAsImage(sender)

        val contentMessageReceipt = sendMessage(
            sender,
            image.plus(
                """
                |
                |${result.url}
                |消息将在 $recallDelaySeconds 秒后撤回
            """.trimMargin()
            )
        )

        withContext(Dispatchers.IO) {
            result.img.close()
        }

        try {
            sendingMessageReceipt.recall()
            delay((15 * 1e3).toLong())
            contentMessageReceipt.recall()
            contentMessageReceipt.recall()
            contentMessageReceipt.recall()
        } catch (_: IllegalStateException) {
        }

        return true
    }
}
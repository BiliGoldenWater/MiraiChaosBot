package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.interfaces.command.ACommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.utils.FileResult
import indi.goldenwater.miraichaosbot.utils.getRandomImage
import indi.goldenwater.miraichaosbot.utils.sendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage

class CommandRandomAcg : ACommandHandler() {
    override suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        val sender: User = messageInfo.sender

        sendMessage(sender, CommandReplyMessage.GettingInfo.s())

        val result: FileResult = getRandomImage()
        val image: Image = result.img.uploadAsImage(sender)

        sendMessage(sender, image.plus("\n").plus(PlainText(result.url)))

        withContext(Dispatchers.IO) {
            result.img.close()
        }

        return true
    }
}
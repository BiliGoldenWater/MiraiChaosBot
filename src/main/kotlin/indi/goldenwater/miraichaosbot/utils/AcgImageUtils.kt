package indi.goldenwater.miraichaosbot.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.buildForwardMessage
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage

suspend fun getRandomImage(): FileResult {
    return httpGetFile(
        httpGetRedirectTarget("https://iw233.cn/api/Random.php") ?: throw Exception("No target location")
    )
}

suspend fun User.sendRandomAcgImage() {
    val result: FileResult = getRandomImage()
    val image: Image = result.file.uploadAsImage(this)

    withContext(Dispatchers.IO) {
        result.file.close()
    }

    this.let {
        this.sendMessageWithoutAt(buildForwardMessage(this) {
            it.bot says image
            it.bot says result.url
        })
    }
}
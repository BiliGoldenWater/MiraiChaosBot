package indi.goldenwater.miraichaosbot.utils

import indi.goldenwater.miraichaosbot.MiraiChaosBot
import kotlinx.coroutines.delay
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import kotlin.math.roundToLong
import kotlin.reflect.jvm.jvmName

data class MessageLog(
    val botId: Long,
    val time: Long,
)

object MessageHistory {
    val messageHistory: MutableList<MessageLog> = mutableListOf()
}

suspend fun User.sendMessageTo(message: String): MessageReceipt<*>? {
    return this.sendMessageTo(PlainText(message))
}

suspend fun User.sendMessageTo(message: Message): MessageReceipt<*>? {
    if (this is Member) {
        return this.sendMessageWithoutAt(At(this).plus("\n").plus(message))
    }
    return this.sendMessageWithoutAt(message)
}

suspend fun User.sendMessageWithoutAt(message: Message): MessageReceipt<*>? {
    MessageHistory.messageHistory.removeIf {
        it.botId == this.bot.id && it.time + (60 * 1e3) < System.currentTimeMillis()
    }
    if (MessageHistory.messageHistory.size >= 10) {
        MiraiChaosBot.logger.warning("Bot: ${this.bot.id} 短时间内发送的消息数量已达上限 1分钟内10条")
        return null
    } else if (
        MessageHistory.messageHistory.any {
            it.botId == this.bot.id && it.time + (5 * 1e3) >= System.currentTimeMillis()
        }
    ) {
        MiraiChaosBot.logger.warning("Bot: ${this.bot.id} 短时间内发送的消息数量已达上限 5秒内1条")
        return null
    }

    MessageHistory.messageHistory.add(MessageLog(this.bot.id, System.currentTimeMillis()))
    delay(1000L + (Math.random() * 2e3).roundToLong())

    if (this is Friend) {
        return this.sendMessage(message)
    } else if (this is Member) {
        return this.group.sendMessage(message)
    }
    throw Exception("Unknown target ${this::class.jvmName}")
}

suspend fun User.sendNudge() {
    delay(1000L + (Math.random() * 2e3).roundToLong())
    if (this is Friend) {
        this.nudge().sendTo(this)
    } else if (this is NormalMember) {
        this.nudge().sendTo(this.group)
    }
}
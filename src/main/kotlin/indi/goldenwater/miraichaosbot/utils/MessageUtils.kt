package indi.goldenwater.miraichaosbot.utils

import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import kotlin.reflect.jvm.jvmName

suspend fun User.sendMessageTo(message: String): MessageReceipt<*> {
    return this.sendMessageTo(PlainText(message))
}

suspend fun User.sendMessageTo(message: Message): MessageReceipt<*> {
    if (this is Member) {
        return this.sendMessageWithoutAt(At(this).plus("\n").plus(message))
    }
    return this.sendMessageWithoutAt(message)
}

suspend fun User.sendMessageWithoutAt(message: Message): MessageReceipt<*> {
    if (this is Friend) {
        return this.sendMessage(message)
    } else if (this is Member) {
        return this.group.sendMessage(message)
    }
    throw Exception("Unknown target ${this::class.jvmName}")
}

suspend fun User.sendNudge() {
    if (this is Friend) {
        this.nudge().sendTo(this)
    } else if (this is NormalMember) {
        this.nudge().sendTo(this.group)
    }
}
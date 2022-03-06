package indi.goldenwater.miraichaosbot.utils

import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import kotlin.reflect.jvm.jvmName

suspend fun sendMessage(target: User, message: String): MessageReceipt<*> {
    return sendMessage(target, PlainText(message))
}

suspend fun sendMessage(target: User, message: Message): MessageReceipt<*> {
    if (target is Friend) {
        return target.sendMessage(message)
    } else if (target is Member) {
        return target.group.sendMessage(At(target).plus("\n").plus(message))
    }
    throw Exception("Unknown target ${target::class.jvmName}")
}
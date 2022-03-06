package indi.goldenwater.miraichaosbot.utils

import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import kotlin.reflect.jvm.jvmName

suspend fun sendMessage(target: User, message: String) {
    sendMessage(target, PlainText(message))
}

suspend fun sendMessage(target: User, message: Message) {
    if (target is Friend) {
        target.sendMessage(message)
        return
    } else if (target is Member) {
        target.group.sendMessage(At(target).plus("\n").plus(message))
        return
    }
    throw Exception("Unknown target ${target::class.jvmName}")
}
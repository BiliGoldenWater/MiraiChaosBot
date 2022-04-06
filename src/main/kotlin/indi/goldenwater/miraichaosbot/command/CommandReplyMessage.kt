package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.interfaces.type.genshin.ArtifactAttribute
import indi.goldenwater.miraichaosbot.api.command.CommandManager.prefix as p
import net.mamoe.mirai.message.data.PlainText
import kotlin.reflect.full.memberProperties

enum class CommandReplyMessage(private val message: String) {
    /*
    命令属性
    p 权限
      m 群员
      a 管理员
      o 群主
      + 及以上
    s 场景
      g 群
      f 好友
    */
    Help(
        """
        |${p}help 显示本消息
        |${p}song search <name> 搜索歌曲
        |${p}song byId <id> 根据歌曲id点歌
        |${p}artifactScore <(${ArtifactAttribute::class.memberProperties.joinToString(separator = "|") { it.name }})-V> 圣遗物分数
        |  例: ${p}artifactScore cd-46.62|cr-3.89|er-6.48|defP-7.29
    """.trimMargin()
    ),

    // |${p}randomAcg 随机图片
    HelpAdmin(
        """
        |${p}getNonactiveMembers addWhiteList <qqNumber1> [qqNumber2] ... 添加用户至白名单 (s:g;p:a+)
        |${p}getNonactiveMembers removeWhiteList <qqNumber1> [qqNumber2] ... 将用户从白名单中移除 (s:g;p:a+)
        |${p}getNonactiveMembers neverTalk 获取从未发言的群成员 (s:g;p:a+)
        |${p}getNonactiveMembers neverTalk <time> 获取在一段时间前加入群且从未发言的群成员 (s:g;p:a+)
        |${p}getNonactiveMembers lastTalkBefore <time> 获取最后发言时间在一段时间前的群成员 (s:g;p:a+)
        |  参数 time 格式: [月:]天
        |  注: 1月直接转换为30天 不接受 [0:]0
    """.trimMargin()
    ),

//    GettingInfo("获取信息中"),

    Added("已添加"),
    Removed("已移除"),

    UnknownUsage("未知的用法, 请输入${p}help查询"),
    GroupOnly("仅可在群内使用"),
    NoPermission("权限不足"),
    AddFailedBy("%s 添加失败 %s"),
    FailedByException("发生了错误"),
    FailedByUnknownReason("未知的错误"),
    ;

    fun s() = toString()
    fun m() = PlainText(toString())

    override fun toString() = message
}
package indi.goldenwater.miraichaosbot.command

import indi.goldenwater.miraichaosbot.api.command.CommandManager.prefix as p
import net.mamoe.mirai.message.data.PlainText

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
        |${p}song <search> <name> 搜索歌曲
        |${p}song <byId> <id> 根据歌曲id点歌
        |${p}randomAcg 随机图片
        |${p}getNonactiveMembers addWhiteList <qqNumber> 添加用户至白名单
        |${p}getNonactiveMembers removeWhiteList <qqNumber> 将用户从白名单中移除
        |${p}getNonactiveMembers neverTalk 获取从未发言的群成员 (s:g)
        |${p}getNonactiveMembers neverTalk <time> 获取在n秒前加入群且从未发言的群成员 (s:g)
        |${p}getNonactiveMembers lastTalkBefore <time> 获取最后发言时间在n秒前的群成员 (s:g)
    """.trimMargin()
    ),

    GettingInfo("获取信息中"),

    Added("已添加"),
    Removed("已移除"),

    GroupOnly("仅可在群内使用"),
    UnknownUsage("未知的用法, 请输入${p}help查询"),
    AddFailedBy("%s 添加失败 %s"),
    FailedByException("发生了错误"),
    FailedByUnknownReason("未知的错误"),
    ;

    fun s() = toString()
    fun m() = PlainText(toString())

    override fun toString() = message
}
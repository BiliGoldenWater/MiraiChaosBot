package indi.goldenwater.miraichaosbot

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.command.CommandHelp
import indi.goldenwater.miraichaosbot.command.CommandRandomAcg
import indi.goldenwater.miraichaosbot.command.Commands
import indi.goldenwater.miraichaosbot.command.memberanalyze.*
import indi.goldenwater.miraichaosbot.command.song.CommandSong
import indi.goldenwater.miraichaosbot.command.song.SubCommandById
import indi.goldenwater.miraichaosbot.command.song.SubCommandSearch
import indi.goldenwater.miraichaosbot.config.Config
import indi.goldenwater.miraichaosbot.listener.OnFriendMessageEvent
import indi.goldenwater.miraichaosbot.listener.OnGroupMessageEvent
import indi.goldenwater.miraichaosbot.utils.initLogger
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object MiraiChaosBot : KotlinPlugin(
    JvmPluginDescription(
        id = "indi.goldenwater.miraichaosbot.MiraiChaosBot",
        name = "MiraiChaosBot",
        version = "1.3.0",
    ) {
        author("Golden_Water")
    }
) {
    override fun onEnable() {
        initLogger(logger)
        Config.reload()

        //region Command register
        CommandManager.addCommand(Commands.Help.r(), CommandHelp())

        CommandManager.addCommand(Commands.Song.r(), CommandSong())
        CommandManager.addCommand(arrayOf(Commands.Song.r(), Commands.ById.r()), SubCommandById())
        CommandManager.addCommand(arrayOf(Commands.Song.r(), Commands.Search.r()), SubCommandSearch())

        CommandManager.addCommand(Commands.RandomAcg.r(), CommandRandomAcg())

        CommandManager.addCommand(Commands.GetNonactiveMembers.r(), CommandMemberAnalyze())
        CommandManager.addCommand(
            arrayOf(Commands.GetNonactiveMembers.r(), Commands.AddWhiteList.r()),
            SubCommandAddWhiteList()
        )
        CommandManager.addCommand(
            arrayOf(Commands.GetNonactiveMembers.r(), Commands.RemoveWhiteList.r()),
            SubCommandRemoveWhiteList()
        )
        CommandManager.addCommand(
            arrayOf(Commands.GetNonactiveMembers.r(), Commands.NeverTalk.r()),
            SubCommandNeverTalk()
        )
        CommandManager.addCommand(
            arrayOf(Commands.GetNonactiveMembers.r(), Commands.LastTalkBefore.r()),
            SubCommandLastTalkBefore()
        )
        //endregion

        //region Event register
        OnFriendMessageEvent.register()
        OnGroupMessageEvent.register()
        //endregion

        logger.info("Plugin loaded!")
    }

    override fun onDisable() {
        CommandManager.removeAll()

        //region Event unregister
        OnFriendMessageEvent.unregister()
        OnGroupMessageEvent.unregister()
        //endregion

        logger.info("Plugin unloaded!")
    }
}
package indi.goldenwater.miraichaosbot

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.command.CommandHelp
import indi.goldenwater.miraichaosbot.command.CommandRandomAcg
import indi.goldenwater.miraichaosbot.command.song.CommandSong
import indi.goldenwater.miraichaosbot.command.song.SubCommandById
import indi.goldenwater.miraichaosbot.command.song.SubCommandSearch
import indi.goldenwater.miraichaosbot.listener.OnFriendMessageEvent
import indi.goldenwater.miraichaosbot.listener.OnGroupMessageEvent
import indi.goldenwater.miraichaosbot.utils.initLogger
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object MiraiChaosBot : KotlinPlugin(
    JvmPluginDescription(
        id = "indi.goldenwater.miraichaosbot.MiraiChaosBot",
        name = "MiraiChaosBot",
        version = "1.2.1",
    ) {
        author("Golden_Water")
    }
) {
    override fun onEnable() {
        initLogger(logger)

        //region Command register
        val cmdHelp = Regex("help", RegexOption.IGNORE_CASE)
        val cmdSong = Regex("song", RegexOption.IGNORE_CASE)
        val cmdById = Regex("byId", RegexOption.IGNORE_CASE)
        val cmdSearch = Regex("search", RegexOption.IGNORE_CASE)
        val cmdRandomAcg = Regex("randomAcg", RegexOption.IGNORE_CASE)

        CommandManager.addCommand(cmdHelp, CommandHelp())

        CommandManager.addCommand(cmdSong, CommandSong())
        CommandManager.addCommand(arrayOf(cmdSong, cmdById), SubCommandById())
        CommandManager.addCommand(arrayOf(cmdSong, cmdSearch), SubCommandSearch())

        CommandManager.addCommand(cmdRandomAcg, CommandRandomAcg())
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
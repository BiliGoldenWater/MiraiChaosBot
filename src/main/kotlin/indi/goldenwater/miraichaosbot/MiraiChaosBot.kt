package indi.goldenwater.miraichaosbot

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.command.CommandHelp
import indi.goldenwater.miraichaosbot.command.song.CommandSong
import indi.goldenwater.miraichaosbot.command.song.SubCommandById
import indi.goldenwater.miraichaosbot.command.song.SubCommandSearch
import indi.goldenwater.miraichaosbot.listener.OnFriendMessageEvent
import indi.goldenwater.miraichaosbot.listener.OnGroupMessageEvent
import indi.goldenwater.miraichaosbot.utils.httpGet
import indi.goldenwater.miraichaosbot.utils.initLogger
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object MiraiChaosBot : KotlinPlugin(
    JvmPluginDescription(
        id = "indi.goldenwater.miraichaosbot.MiraiChaosBot",
        name = "MiraiChaosBot",
        version = "1.0.0",
    ) {
        author("Golden_Water")
    }
) {
    override fun onEnable() {
        initLogger(logger)

        //region Command register
        CommandSong().let {
            CommandManager.addCommand(Regex("song", RegexOption.IGNORE_CASE), it)

            it.addSubCommand(Regex("byId", RegexOption.IGNORE_CASE), SubCommandById())
            it.addSubCommand(Regex("search", RegexOption.IGNORE_CASE), SubCommandSearch())
        }

        CommandManager.addCommand(Regex("help", RegexOption.IGNORE_CASE), CommandHelp())
        //endregion

        //region Event register
        OnFriendMessageEvent.register()
        OnGroupMessageEvent.register()
        //endregion

        logger.info("Plugin loaded!")
    }

    override fun onDisable() {
        //region Command unregister
        CommandManager.removeCommand(Regex("help", RegexOption.IGNORE_CASE))

        CommandManager.getCommandHandler("song").let {
            it?.removeSubCommand(Regex("byId", RegexOption.IGNORE_CASE))
            it?.removeSubCommand(Regex("search", RegexOption.IGNORE_CASE))
        }
        CommandManager.removeCommand(Regex("song", RegexOption.IGNORE_CASE))
        //endregion

        //region Event unregister
        OnFriendMessageEvent.unregister()
        OnGroupMessageEvent.unregister()
        //endregion

        logger.info("Plugin unloaded!")
    }
}
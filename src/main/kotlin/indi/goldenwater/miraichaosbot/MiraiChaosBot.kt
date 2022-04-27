package indi.goldenwater.miraichaosbot

import indi.goldenwater.miraichaosbot.api.command.CommandManager
import indi.goldenwater.miraichaosbot.command.CommandArtifactScore
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
import indi.goldenwater.miraichaosbot.listener.OnNudgeEvent
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import java.awt.Font
import java.awt.GraphicsEnvironment

var json: Json = Json {
    ignoreUnknownKeys = true
}

var defaultFont: Font? = null

object MiraiChaosBot : KotlinPlugin(
    JvmPluginDescription(
        id = "indi.goldenwater.miraichaosbot.MiraiChaosBot",
        name = "MiraiChaosBot",
        version = "1.7.0",
    ) {
        author("Golden_Water")
    }
) {
    override fun onEnable() {
        defaultFont = Font.createFont(
            Font.TRUETYPE_FONT,
            MiraiChaosBot.getResourceAsStream("assets/font/SourceHanSansCN-VF.ttf")
        )
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(defaultFont)
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

        CommandManager.addCommand(
            Commands.ArtifactScore.r(),
            CommandArtifactScore()
        )
        //endregion

        //region Event register
        OnFriendMessageEvent.register()
        OnGroupMessageEvent.register()
        OnNudgeEvent.register()
        //endregion

        logger.info("Plugin loaded!")
    }

    override fun onDisable() {
        CommandManager.removeAll()

        //region Event unregister
        OnFriendMessageEvent.unregister()
        OnGroupMessageEvent.unregister()
        OnNudgeEvent.unregister()
        //endregion

        logger.info("Plugin unloaded!")
    }
}
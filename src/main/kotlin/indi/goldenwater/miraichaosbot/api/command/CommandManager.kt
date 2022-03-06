package indi.goldenwater.miraichaosbot.api.command

import indi.goldenwater.miraichaosbot.api.interfaces.command.DMessageInfo
import indi.goldenwater.miraichaosbot.api.interfaces.command.ICommandHandler
import indi.goldenwater.miraichaosbot.api.interfaces.type.CommandHandlers

object CommandManager {
    var prefix: String = "&"

    private val commandHandlers: CommandHandlers = mutableMapOf()

    /**
     * Return true if [message] is a correct command
     * */
    suspend fun processMessage(senderInfo: DMessageInfo, message: String): Boolean {
        if (!message.startsWith(prefix)) return false

        val command = message.removePrefix(prefix).split(" ")

        for ((cmd, handler) in commandHandlers) {
            if (cmd.matches(command[0])) {
                return handler.callCommand(senderInfo, command[0], command.drop(1).toTypedArray())
            }
        }

        return false
    }

    fun addCommand(command: Regex, handler: ICommandHandler) {
        commandHandlers[command] = handler
    }


    fun getCommandHandler(command: Regex): ICommandHandler? {
        return commandHandlers[command]
    }

    fun getCommandHandler(command: String): ICommandHandler? {
        return commandHandlers
            .filter { it.key.matches(command) }
            .firstNotNullOfOrNull { it.value }
    }

    fun removeCommand(command: Regex) {
        commandHandlers.remove(command)
    }
}
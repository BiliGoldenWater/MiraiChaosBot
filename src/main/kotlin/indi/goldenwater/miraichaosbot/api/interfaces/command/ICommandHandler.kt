package indi.goldenwater.miraichaosbot.api.interfaces.command

import indi.goldenwater.miraichaosbot.api.interfaces.type.TCommandHandlers

interface ICommandHandler {
    val subCommandHandlers: TCommandHandlers

    /**
     * Process command, return true when correct usage
     */
    suspend fun onCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean

    /**
     * Call command/subCommand, return true when correct usage
     */
    suspend fun callCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean

    fun addSubCommand(command: Regex, commandHandler: ICommandHandler)

    fun removeSubCommand(command: Regex)
}
package indi.goldenwater.miraichaosbot.api.interfaces.command

import indi.goldenwater.miraichaosbot.api.interfaces.type.CommandHandlers

abstract class ACommandHandler : ICommandHandler {
    override val subCommandHandlers: CommandHandlers = mutableMapOf()

    override suspend fun callCommand(messageInfo: DMessageInfo, command: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return onCommand(messageInfo, command, args)
        }

        for ((key, value) in subCommandHandlers) {
            if (key.matches(args[0])) {
                return value.callCommand(messageInfo, args[0], args.drop(1).toTypedArray())
            }
        }

        return onCommand(messageInfo, command, args)
    }

    override fun addSubCommand(command: Regex, commandHandler: ICommandHandler) {
        subCommandHandlers[command] = commandHandler
    }

    override fun removeSubCommand(command: Regex) {
        subCommandHandlers.remove(command)
    }
}
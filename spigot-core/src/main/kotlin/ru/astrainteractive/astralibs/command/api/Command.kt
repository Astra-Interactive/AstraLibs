package ru.astrainteractive.astralibs.command.api

import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.command.registerCommand

/**
 * Command interface is a store for your command
 *
 * It should contain [CommandParser], [CommandExecutor], [CommandParser.ResultHandler]
 */
interface Command {

    fun register(plugin: JavaPlugin)

    companion object {
        /**
         * Register command and execute it with default hierarchy
         */
        fun <R : Any, I : Any> registerDefault(
            plugin: JavaPlugin,
            commandParser: CommandParser<R>,
            commandExecutor: CommandExecutor<I>,
            resultHandler: CommandParser.ResultHandler<R>,
            transform: (R) -> I?
        ) {
            plugin.registerCommand(commandParser.alias) {
                commandParser.parse(args, sender)
                    .also { resultHandler.handle(sender, it) }
                    .let(transform)
                    ?.let(commandExecutor::execute)
            }
        }
    }
}

package ru.astrainteractive.astralibs.command.api.util

import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext
import ru.astrainteractive.astralibs.command.api.error.ErrorHandler
import ru.astrainteractive.astralibs.command.api.executor.CommandExecutor
import ru.astrainteractive.astralibs.command.api.parser.CommandParser

object PluginExt {
    fun <T : Any> JavaPlugin.registerCommand(
        alias: String,
        commandParser: CommandParser<T, BukkitCommandContext>,
        commandExecutor: CommandExecutor<T>,
        errorHandler: ErrorHandler<BukkitCommandContext>
    ) {
        val javaCommand = getCommand(alias) ?: error("Command with alias $alias not found")
        javaCommand.setExecutor { sender, bukkitCommand, label, args ->
            val commandContext = BukkitCommandContext(
                sender = sender,
                command = bukkitCommand,
                label = label,
                args = args
            )
            val parseResult = runCatching {
                commandParser.parse(commandContext)
            }
            parseResult.onFailure { throwable ->
                errorHandler.handle(commandContext, throwable)
            }
            parseResult.onSuccess(commandExecutor::execute)
            true
        }
    }
}

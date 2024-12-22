package ru.astrainteractive.astralibs.command.api.util

import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.command.api.command.BukkitTabCompleter
import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext
import ru.astrainteractive.astralibs.command.api.error.ErrorHandler
import ru.astrainteractive.astralibs.command.api.executor.CommandExecutor
import ru.astrainteractive.astralibs.command.api.parser.CommandParser

object PluginExt {
    fun <T : Any> JavaPlugin.setCommandExecutor(
        alias: String,
        commandParser: CommandParser<T, BukkitCommandContext>,
        commandExecutor: CommandExecutor<T>,
        errorHandler: ErrorHandler<BukkitCommandContext>
    ) {
        val pluginCommand = getCommand(alias) ?: error("Command with alias $alias not found")
        pluginCommand.setExecutor { sender, bukkitCommand, label, args ->
            val ctx = BukkitCommandContext(
                sender = sender,
                command = bukkitCommand,
                label = label,
                args = args
            )
            runCatching { commandParser.parse(ctx) }
                .onFailure { throwable -> errorHandler.handle(ctx, throwable) }
                .onSuccess(commandExecutor::execute)
            true
        }
    }

    fun JavaPlugin.setCommandExecutor(
        alias: String,
        commandExecutor: CommandExecutor<BukkitCommandContext>,
        errorHandler: ErrorHandler<BukkitCommandContext>
    ) {
        val javaCommand = getCommand(alias) ?: error("Command with alias $alias not found")
        javaCommand.setExecutor { sender, bukkitCommand, label, args ->
            val ctx = BukkitCommandContext(
                sender = sender,
                command = bukkitCommand,
                label = label,
                args = args
            )
            runCatching { commandExecutor.execute(ctx) }
                .onFailure { errorHandler.handle(ctx, it) }
            true
        }
    }

    @Suppress("UnusedPrivateMember")
    private fun JavaPlugin.setCommandTabCompleter(alias: String, tabCompleter: BukkitTabCompleter) {
        val pluginCommand = getCommand(alias) ?: error("Command $alias not found!")
        pluginCommand.setTabCompleter { commandSender, command, label, args ->
            val ctx = BukkitCommandContext(
                sender = commandSender,
                command = command,
                label = label,
                args = args
            )
            tabCompleter.onTabComplete(ctx)
        }
    }
}

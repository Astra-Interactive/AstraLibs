package ru.astrainteractive.astralibs.command.api

import org.bukkit.plugin.java.JavaPlugin

/**
 * [CommandFactory] will create default [Command] instance. Use with delegates
 */
interface CommandFactory {
    @Suppress("LongParameterList")
    fun <R : Any, I : Any> create(
        plugin: JavaPlugin,
        alias: String,
        commandParser: CommandParser<R>,
        commandExecutor: CommandExecutor<I>,
        resultHandler: Command.ResultHandler<R>,
        mapper: Command.Mapper<R, I>
    ): Command<R, I>
}

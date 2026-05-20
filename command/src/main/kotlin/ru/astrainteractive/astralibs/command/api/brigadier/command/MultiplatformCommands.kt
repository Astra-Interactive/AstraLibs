package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender

/**
 * Platform-specific factory for Brigadier builder nodes and command-sender resolution.
 *
 * Implement once per target platform to adapt the Brigadier API to [MultiplatformCommand].
 */
interface MultiplatformCommands {
    fun literal(literal: String): LiteralArgumentBuilder<Any>

    fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<Any, T>

    /** Resolves the [KCommandSender] from a Brigadier [CommandContext]. */
    fun getSender(context: CommandContext<*>): KCommandSender
}

package ru.astrainteractive.astralibs.command.brigadier.api

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import ru.astrainteractive.astralibs.command.sender.model.KCommandSender

/**
 * Platform-specific factory for Brigadier builder nodes and command-sender resolution.
 *
 * Implement once per target platform to adapt the Brigadier API to
 * [ru.astrainteractive.astralibs.command.brigadier.MultiplatformCommand].
 */
public interface MultiplatformCommands {
    public fun literal(literal: String): LiteralArgumentBuilder<Any>

    public fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<Any, T>

    /** Resolves the [KCommandSender] from a Brigadier [CommandContext]. */
    public fun getSender(context: CommandContext<*>): KCommandSender
}

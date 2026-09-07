package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender

/** Builds plain Brigadier nodes, so the DSL can be exercised without a running server. */
internal class FakeMultiplatformCommands : MultiplatformCommands {
    override fun literal(literal: String): LiteralArgumentBuilder<Any> {
        return LiteralArgumentBuilder.literal(literal)
    }

    override fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<Any, T> {
        return RequiredArgumentBuilder.argument(name, argumentType)
    }

    override fun getSender(context: CommandContext<*>): KCommandSender {
        error("Sender resolution is not part of node building")
    }
}

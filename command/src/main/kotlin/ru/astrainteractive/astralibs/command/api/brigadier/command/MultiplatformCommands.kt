package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender

interface MultiplatformCommands<CommandSourceStack> {
    fun literal(literal: String): LiteralArgumentBuilder<CommandSourceStack>
    fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<CommandSourceStack, T>

    fun getSender(context: CommandContext<*>): KCommandSender
}

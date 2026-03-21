package ru.astrainteractive.astralibs.command.brigadier.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.command.api.brigadier.command.MultiplatformCommands
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KPlayerKCommandSender
import ru.astrainteractive.astralibs.command.brigadier.sender.NeoForgeRconKCommandSender
import ru.astrainteractive.astralibs.command.brigadier.sender.NeoForgeServerKCommandSender
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer

@Suppress("UNCHECKED_CAST")
class NeoForgeMultiplatformCommands : MultiplatformCommands {
    override fun literal(literal: String): LiteralArgumentBuilder<Any> {
        return Commands.literal(literal) as LiteralArgumentBuilder<Any>
    }

    override fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<Any, T> {
        return Commands.argument(name, argumentType) as RequiredArgumentBuilder<Any, T>
    }

    override fun getSender(context: CommandContext<*>): KCommandSender {
        val commandSourceStack = context.source as CommandSourceStack

        return when (val source = commandSourceStack.source) {
            is ServerPlayer -> {
                KPlayerKCommandSender(source.asOnlineMinecraftPlayer())
            }

            is RconConsoleSource -> {
                NeoForgeRconKCommandSender(source)
            }

            is DedicatedServer,
            is MinecraftServer -> {
                NeoForgeServerKCommandSender(source)
            }

            else -> error("Could not wrap sender ${commandSourceStack.source.javaClass}")
        }
    }
}

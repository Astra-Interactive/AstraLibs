package ru.astrainteractive.astralibs.brigadier

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
import ru.astrainteractive.astralibs.brigadier.sender.MinecraftRconKCommandSender
import ru.astrainteractive.astralibs.brigadier.sender.MinecraftServerKCommandSender
import ru.astrainteractive.astralibs.command.brigadier.api.MultiplatformCommands
import ru.astrainteractive.astralibs.command.sender.model.KCommandSender
import ru.astrainteractive.astralibs.command.sender.model.KPlayerKCommandSender
import ru.astrainteractive.astralibs.server.player.MinecraftOnlineKPlayer
import ru.astrainteractive.astralibs.util.publicSource

/**
 * Vanilla Minecraft implementation of [MultiplatformCommands].
 * Maps [CommandSourceStack] to the appropriate [KCommandSender] subtype for players, RCON, and server.
 */
@Suppress("UNCHECKED_CAST")
public class MinecraftMultiplatformCommands : MultiplatformCommands {
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
        return when (val source = commandSourceStack.publicSource) {
            is ServerPlayer -> {
                KPlayerKCommandSender(MinecraftOnlineKPlayer(source))
            }

            is RconConsoleSource -> {
                MinecraftRconKCommandSender(source)
            }

            is DedicatedServer,
            is MinecraftServer -> {
                MinecraftServerKCommandSender(source)
            }

            else -> error("Could not wrap sender ${source.javaClass}")
        }
    }
}

package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KPlayerKCommandSender
import ru.astrainteractive.astralibs.command.api.brigadier.sender.PaperConsoleKCommandSender
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer

class PaperMultiplatformCommands : MultiplatformCommands<CommandSourceStack> {
    override fun literal(literal: String): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal(literal)
    }

    override fun <T : Any> argument(
        name: String,
        argumentType: ArgumentType<T>
    ): RequiredArgumentBuilder<CommandSourceStack, T> {
        return Commands.argument(name, argumentType)
    }

    override fun getSender(context: CommandContext<*>): KCommandSender {
        val source = context.source as CommandSourceStack
        return when (val sender = source.sender) {
            is RemoteConsoleCommandSender, is ConsoleCommandSender -> {
                PaperConsoleKCommandSender(sender)
            }

            is Player -> {
                KPlayerKCommandSender(sender.asOnlineMinecraftPlayer())
            }

            else -> error("Could not wrap sender ${sender.javaClass}")
        }
    }
}

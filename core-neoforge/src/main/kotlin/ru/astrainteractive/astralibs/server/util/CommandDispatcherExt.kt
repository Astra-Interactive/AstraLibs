package ru.astrainteractive.astralibs.server.util

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import net.neoforged.neoforge.server.ServerLifecycleHooks
import ru.astrainteractive.astralibs.command.brigadier.command.command
import ru.astrainteractive.astralibs.server.KCommandDispatcher

fun MinecraftServer.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.commands.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

fun RconConsoleSource.asKCommandDispatcher() = KCommandDispatcher { command ->
    ServerLifecycleHooks.getCurrentServer()
        ?.commands
        ?.performPrefixedCommand(
            this.createCommandSourceStack(),
            command
        )
}

fun ServerPlayer.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.server.commands.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

package ru.astrainteractive.astralibs.server.util

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.server.KCommandDispatcher

/** Adapts this [MinecraftServer] as a [KCommandDispatcher]. */
fun MinecraftServer.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.commands.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

/** Adapts this [RconConsoleSource] as a [KCommandDispatcher]. Does nothing if the server is unavailable. */
fun RconConsoleSource.asKCommandDispatcher() = KCommandDispatcher { command ->
    MinecraftUtil.serverOrNull
        ?.commands
        ?.performPrefixedCommand(
            this.createCommandSourceStack(),
            command
        )
}

/** Adapts this [ServerPlayer] as a [KCommandDispatcher]. Does nothing if the player's server reference is null. */
fun ServerPlayer.asKCommandDispatcher() = KCommandDispatcher { command ->
    MinecraftUtil.serverOrNull?.commands?.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

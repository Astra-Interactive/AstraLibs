package ru.astrainteractive.astralibs.server.util

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.util.publicServer

fun MinecraftServer.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.commands.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

fun RconConsoleSource.asKCommandDispatcher() = KCommandDispatcher { command ->
    MinecraftUtil.serverOrNull
        ?.commands
        ?.performPrefixedCommand(
            this.createCommandSourceStack(),
            command
        )
}

fun ServerPlayer.asKCommandDispatcher() = KCommandDispatcher { command ->
    publicServer?.commands?.performPrefixedCommand(
        this.createCommandSourceStack(),
        command
    )
}

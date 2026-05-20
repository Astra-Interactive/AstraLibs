package ru.astrainteractive.astralibs.server.util

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.rcon.RconConsoleSource
import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.KAudience

/**
 * Adapts this [Player] as a [KAudience].
 *
 * Messages are shown in the chat area, not the action bar.
 */
fun Player.asKAudience() = KAudience { component ->
    displayClientMessage(component.toNative(), false)
}

/** Adapts this [CommandSourceStack] as a [KAudience]. */
fun CommandSourceStack.asKAudience() = KAudience { component ->
    this.sendSystemMessage(component.toNative())
}

/** Adapts this [MinecraftServer] as a [KAudience]. */
fun MinecraftServer.asKAudience() = KAudience { component ->
    sendSystemMessage(component.toNative())
}

/** Adapts this [RconConsoleSource] as a [KAudience]. */
fun RconConsoleSource.asKAudience() = KAudience { component ->
    sendSystemMessage(component.toNative())
}

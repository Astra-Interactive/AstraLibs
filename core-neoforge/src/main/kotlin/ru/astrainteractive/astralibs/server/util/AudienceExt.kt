package ru.astrainteractive.astralibs.server.util

import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.rcon.RconConsoleSource
import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.KAudience

fun Player.asKAudience() = KAudience { component ->
    this.sendSystemMessage(component.toNative())
}

fun CommandSourceStack.asKAudience() = KAudience { component ->
    this.sendSystemMessage(component.toNative())
}

fun MinecraftServer.asKAudience() = KAudience { component ->
    sendSystemMessage(component.toNative())
}

fun RconConsoleSource.asKAudience() = KAudience { component ->
    sendSystemMessage(component.toNative())
}

package ru.astrainteractive.astralibs.server.util

import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.KAudience

fun Player.asAudience() = KAudience { component ->
    this.sendSystemMessage(component.toNative())
}

fun CommandSourceStack.asAudience() = KAudience { component ->
    this.sendSystemMessage(component.toNative())
}

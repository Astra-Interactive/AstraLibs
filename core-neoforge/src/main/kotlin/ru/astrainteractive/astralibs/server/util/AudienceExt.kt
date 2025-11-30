package ru.astrainteractive.astralibs.server.util

import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun Player.asAudience() = Audience { component ->
    this.sendSystemMessage(component.toNative())
}

fun CommandSourceStack.asAudience() = Audience { component ->
    this.sendSystemMessage(component.toNative())
}

fun OnlineMinecraftPlayer.asAudience() = Audience { component ->
    val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: return@Audience
    player.sendSystemMessage(component.toNative())
}

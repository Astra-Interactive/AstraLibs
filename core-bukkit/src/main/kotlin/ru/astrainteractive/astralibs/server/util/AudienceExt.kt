package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot

fun OnlineMinecraftPlayerSnapshot.asAudience(): Audience {
    val player = Bukkit.getPlayer(uuid) ?: error("$this is not online")
    return player.asAudience()
}

fun Player.asAudience() = Audience { component ->
    val player = this
    player.sendMessage(component)
}
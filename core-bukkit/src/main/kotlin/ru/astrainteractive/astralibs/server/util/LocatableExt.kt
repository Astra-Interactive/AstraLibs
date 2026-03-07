package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot

fun OnlineMinecraftPlayerSnapshot.asLocatable(): Locatable {
    val player = Bukkit.getPlayer(uuid) ?: error("$this is not online")
    return player.asLocatable()
}

fun Player.asLocatable() = Locatable {
    val player = this
    KLocation(
        x = player.location.x,
        y = player.location.y,
        z = player.location.z,
        worldName = player.location.world.name
    )
}
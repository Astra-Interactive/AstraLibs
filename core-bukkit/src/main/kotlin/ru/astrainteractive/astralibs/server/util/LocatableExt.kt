package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.Location
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun OnlineMinecraftPlayer.asLocatable() = Locatable {
    val player = Bukkit.getPlayer(uuid) ?: error("$this is not online")
    Location(
        x = player.location.x,
        y = player.location.y,
        z = player.location.z,
        worldName = player.location.world.name
    )
}

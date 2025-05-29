package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.Location
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun OnlineMinecraftPlayer.asTeleportable() = Teleportable { location ->
    val player = Bukkit.getPlayer(uuid) ?: error("$this is not online")
    val world = Bukkit.getWorld(location.worldName) ?: error("${location.worldName} world is not found")
    player.teleport(
        Location(
            world,
            location.x,
            location.y,
            location.z,
        )
    )
}

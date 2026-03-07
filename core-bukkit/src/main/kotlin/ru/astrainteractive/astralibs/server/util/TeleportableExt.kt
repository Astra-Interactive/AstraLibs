package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot

fun OnlineMinecraftPlayerSnapshot.asTeleportable(): Teleportable {
    val player = Bukkit.getPlayer(uuid) ?: error("$this is not online")
    return player.asTeleportable()
}

fun Player.asTeleportable() = Teleportable { location ->
    val player = this
    val world = Bukkit.getWorld(location.worldName) ?: error("${location.worldName} world is not found")
    teleport(
        Location(
            world,
            player.location.x,
            player.location.y,
            player.location.z,
        )
    )
}
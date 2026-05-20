package ru.astrainteractive.astralibs.server.util

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.KLocation

/** Adapts this [Player] as a [Locatable] that snapshots the player's position on each call. */
fun Player.asLocatable() = Locatable {
    val player = this
    KLocation(
        x = player.location.x,
        y = player.location.y,
        z = player.location.z,
        worldName = player.location.world.name
    )
}

package ru.astrainteractive.astralibs.server.util

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.KLocation

fun Player.asLocatable() = Locatable {
    val player = this
    KLocation(
        x = player.location.x,
        y = player.location.y,
        z = player.location.z,
        worldName = player.location.world.name
    )
}

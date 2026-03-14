package ru.astrainteractive.astralibs.server.util

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Audience

fun Player.asAudience() = Audience { component ->
    val player = this
    player.sendMessage(component)
}

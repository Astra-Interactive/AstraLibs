package ru.astrainteractive.astralibs.server.util

import net.kyori.adventure.audience.Audience
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.KAudience

fun Player.asKAudience() = KAudience { component ->
    val player = this
    player.sendMessage(component)
}

fun Audience.asKAudience() = KAudience { component ->
    val audience = this
    audience.sendMessage(component)
}

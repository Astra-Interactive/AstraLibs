package ru.astrainteractive.astralibs.server.util

import net.kyori.adventure.audience.Audience
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.KAudience

/** Adapts this [Player] as a [KAudience]. */
fun Player.asKAudience() = KAudience { component ->
    val player = this
    player.sendMessage(component)
}

/** Adapts this [Audience] as a [KAudience]. */
fun Audience.asKAudience() = KAudience { component ->
    val audience = this
    audience.sendMessage(component)
}

package ru.astrainteractive.astralibs.menu.holder

import org.bukkit.entity.Player

/** Holds the [Player] viewing a menu. Extend to carry per-player state across renders. */
interface PlayerHolder {
    val player: Player
}

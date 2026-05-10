package ru.astrainteractive.astralibs.menu.holder

import org.bukkit.entity.Player

/**
 * Holds the [Player] viewing a menu.
 *
 * Extend to attach per-player state that should persist across renders (e.g. current page, active filters).
 */
interface PlayerHolder {
    val player: Player
}

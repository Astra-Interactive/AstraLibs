package ru.astrainteractive.astralibs.menu

import org.bukkit.entity.Player

/**
 * PlayerMenuUtility data class
 *
 * Can be extended to add stuff like opened page etc
 */
interface IPlayerHolder {
    val player: Player
}
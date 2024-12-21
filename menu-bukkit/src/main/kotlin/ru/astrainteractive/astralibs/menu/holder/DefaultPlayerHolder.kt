package ru.astrainteractive.astralibs.menu.holder

import org.bukkit.entity.Player

/**
 * Default implementation of [PlayerHolder]
 */
@JvmInline
value class DefaultPlayerHolder(override val player: Player) : PlayerHolder

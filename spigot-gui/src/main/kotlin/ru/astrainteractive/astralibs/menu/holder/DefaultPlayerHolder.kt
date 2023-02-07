package ru.astrainteractive.astralibs.menu.holder

import org.bukkit.entity.Player

/**
 * Default implementatin of [PlayerHolder]
 */
@JvmInline
value class DefaultPlayerHolder(override val player: Player) : PlayerHolder
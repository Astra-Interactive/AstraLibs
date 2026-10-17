package ru.astrainteractive.astralibs.menu.holder

import org.bukkit.entity.Player

/** Default [PlayerHolder] implementation. */
public data class DefaultPlayerHolder(
    override val player: Player
) : PlayerHolder

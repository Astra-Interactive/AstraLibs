package ru.astrainteractive.astralibs.server.permission

import org.bukkit.permissions.Permissible

/** Adapts this [Permissible] as a [KPermissible]. */
public fun Permissible.asKPermissible(): KPermissible {
    return BukkitKPermissible(this)
}

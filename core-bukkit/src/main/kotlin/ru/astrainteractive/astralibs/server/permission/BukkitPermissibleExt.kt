package ru.astrainteractive.astralibs.server.permission

import org.bukkit.permissions.Permissible

/** Adapts this [Permissible] as a [KPermissible]. */
fun Permissible.asKPermissible(): KPermissible {
    return BukkitKPermissible(this)
}

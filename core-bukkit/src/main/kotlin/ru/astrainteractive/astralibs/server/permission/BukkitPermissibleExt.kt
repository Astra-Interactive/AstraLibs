package ru.astrainteractive.astralibs.server.permission

import org.bukkit.permissions.Permissible
import ru.astrainteractive.astralibs.server.permission.api.KPermissible

/** Adapts this [Permissible] as a [KPermissible]. */
public fun Permissible.asKPermissible(): KPermissible {
    return BukkitKPermissible(this)
}

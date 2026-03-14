package ru.astrainteractive.astralibs.server.permission

import org.bukkit.permissions.Permissible

fun Permissible.asKPermissible(): KPermissible {
    return BukkitKPermissible(this)
}

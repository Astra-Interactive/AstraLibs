package ru.astrainteractive.astralibs.permission

object BukkitPermissibleExt {
    fun org.bukkit.permissions.Permissible.toPermissible(): BukkitPermissible {
        return BukkitPermissible(this)
    }
}

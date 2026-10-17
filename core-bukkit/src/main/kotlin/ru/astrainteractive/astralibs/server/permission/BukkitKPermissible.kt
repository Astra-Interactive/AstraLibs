package ru.astrainteractive.astralibs.server.permission

import org.bukkit.permissions.Permissible

/**
 * [KPermissible] backed by a Bukkit [Permissible].
 */
class BukkitKPermissible(
    private val bukkitPermissible: Permissible
) : KPermissible {

    override fun hasPermission(permission: Permission): Boolean {
        return bukkitPermissible.hasPermission(permission.value)
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        return bukkitPermissible.effectivePermissions
            .filter { info -> info.permission.startsWith(permission.value) }
            .map { info -> info.permission }
            .map { node -> node.replace("${permission.value}.", "") }
            .mapNotNull { size -> size.toIntOrNull() }
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).maxOrNull()
    }

    override fun minPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).minOrNull()
    }
}

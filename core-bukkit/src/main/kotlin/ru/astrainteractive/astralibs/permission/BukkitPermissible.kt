package ru.astrainteractive.astralibs.permission

class BukkitPermissible(
    private val bukkitPermissible: org.bukkit.permissions.Permissible
) : Permissible {

    override fun hasPermission(permission: Permission): Boolean {
        return bukkitPermissible.hasPermission(permission.value)
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).maxOrNull()
    }

    override fun minPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).minOrNull()
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        return bukkitPermissible.effectivePermissions
            .filter { it.permission.startsWith(permission.value) }
            .map { it.permission }
            .map { it.replace("${permission.value}.", "") }
            .mapNotNull { it.toIntOrNull() }
    }
}

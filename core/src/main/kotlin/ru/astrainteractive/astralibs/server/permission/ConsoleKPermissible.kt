package ru.astrainteractive.astralibs.server.permission

/**
 * A [KPermissible] implementation that represents the server console.
 *
 * The console is treated as having every permission and no size constraints, mirroring
 * the behaviour of Bukkit's `ConsoleCommandSender`.
 */
object ConsoleKPermissible : KPermissible {
    override fun hasPermission(permission: Permission): Boolean {
        return true
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        return null
    }

    override fun minPermissionSize(permission: Permission): Int? {
        return null
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        return emptyList()
    }
}

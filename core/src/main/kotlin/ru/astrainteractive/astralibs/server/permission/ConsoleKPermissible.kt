package ru.astrainteractive.astralibs.server.permission

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

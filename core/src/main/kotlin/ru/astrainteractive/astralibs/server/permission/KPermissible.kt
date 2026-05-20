package ru.astrainteractive.astralibs.server.permission

/** An entity (e.g. a player) whose permissions can be queried. */
interface KPermissible {

    fun hasPermission(permission: Permission): Boolean

    /**
     * Returns the max integer suffix for `<permission.value>.<int>` nodes, or `null` if none exist.
     */
    fun maxPermissionSize(permission: Permission): Int?

    /**
     * Returns the min integer suffix for `<permission.value>.<int>` nodes, or `null` if none exist.
     */
    fun minPermissionSize(permission: Permission): Int?

    /** Returns all integer suffixes for `<permission.value>.<int>` nodes. */
    fun permissionSizes(permission: Permission): List<Int>
}

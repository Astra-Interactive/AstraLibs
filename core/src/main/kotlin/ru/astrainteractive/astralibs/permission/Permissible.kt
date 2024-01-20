package ru.astrainteractive.astralibs.permission

interface Permissible {
    /**
     * Checks whether player has permission or not
     */
    fun hasPermission(permission: Permission): Boolean

    /**
     * Get max permission size: com.example.permission.7
     */
    fun maxPermissionSize(permission: Permission): Int?

    /**
     * Get min permission size: com.example.permission.7
     */
    fun minPermissionSize(permission: Permission): Int?

    /**
     * Get all permission sizes: com.example.permission.7
     */
    fun permissionSizes(permission: Permission): List<Int>
}

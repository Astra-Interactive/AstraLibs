package ru.astrainteractive.astralibs.permission

import java.util.UUID

interface PermissionManager {
    /**
     * Checks whether player has permission or not
     */
    fun hasPermission(uuid: UUID, permission: Permission): Boolean

    /**
     * Get max permission size: com.example.permission.7
     */
    fun maxPermissionSize(uuid: UUID, permission: Permission): Int?

    /**
     * Get min permission size: com.example.permission.7
     */
    fun minPermissionSize(uuid: UUID, permission: Permission): Int?

    /**
     * Get all permission sizes: com.example.permission.7
     */
    fun permissionSizes(uuid: UUID, permission: Permission): List<Int>
}

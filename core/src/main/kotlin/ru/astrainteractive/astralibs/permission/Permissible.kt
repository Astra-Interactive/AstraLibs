package ru.astrainteractive.astralibs.permission

/**
 * Represents an entity (e.g., a player or user) that has permissions in the system.
 * This interface defines methods to check and retrieve permission-related information,
 * such as checking if a specific permission is granted and retrieving the allowed size range.
 */
interface Permissible {

    /**
     * Checks whether the entity has the specified permission.
     *
     * @param permission The [Permission] to check.
     * @return true if the entity has the permission; {@code false} otherwise.
     */
    fun hasPermission(permission: Permission): Boolean

    /**
     * Retrieves the maximum allowed size for a given permission, if defined.
     *
     * @param permission The [Permission] to check.
     * @return The maximum size allowed for this permission, or null if not applicable.
     */
    fun maxPermissionSize(permission: Permission): Int?

    /**
     * Retrieves the minimum allowed size for a given permission, if defined.
     *
     * @param permission The [Permission] to check.
     * @return The minimum size allowed for this permission, or null if not applicable.
     */
    fun minPermissionSize(permission: Permission): Int?

    /**
     * Returns a list of allowed sizes for the specified permission.
     * This can represent specific size constraints, such as the number of slots or levels.
     *
     * @param permission The [Permission] to check.
     * @return A list of integers representing the allowed sizes for this permission.
     */
    fun permissionSizes(permission: Permission): List<Int>
}

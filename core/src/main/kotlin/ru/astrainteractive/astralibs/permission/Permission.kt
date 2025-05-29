package ru.astrainteractive.astralibs.permission

/**
 * Represents a permission identifier in the system.
 * Permissions define access levels or constraints associated with certain actions or resources.
 */
interface Permission {

    /**
     * The string value that uniquely identifies the permission.
     * Typically used to compare permissions or register them within a permission system.
     */
    val value: String
}

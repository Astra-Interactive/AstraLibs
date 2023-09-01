package ru.astrainteractive.astralibs.util

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface Permission {
    /**
     * Permission value: com.example.permission
     */
    val value: String

    /**
     * Checks whether player has permission or not
     */
    fun hasPermission(player: CommandSender): Boolean {
        return player.hasPermission(value)
    }

    /**
     * Get max permission size: com.example.permission.7
     */
    fun maxPermissionSize(player: Player): Int? {
        return permissionSizes(player).maxOrNull()
    }

    /**
     * Get min permission size: com.example.permission.7
     */
    fun minPermissionSize(player: Player): Int? {
        return permissionSizes(player).minOrNull()
    }

    /**
     * Get all permission sizes: com.example.permission.7
     */
    fun permissionSizes(player: Player): List<Int> {
        return player.effectivePermissions
            .filter { it.permission.startsWith(value) }
            .map { it.permission }
            .map { it.replace("$value.", "") }
            .mapNotNull { it.toIntOrNull() }
    }
}

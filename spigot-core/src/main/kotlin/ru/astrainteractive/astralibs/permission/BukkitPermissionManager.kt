package ru.astrainteractive.astralibs.permission

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class BukkitPermissionManager : PermissionManager {
    private val UUID.toOfflinePlayer: Player?
        get() = Bukkit.getPlayer(this)

    override fun hasPermission(uuid: UUID, permission: Permission): Boolean {
        val player = uuid.toOfflinePlayer ?: return false
        return player.hasPermission(permission.value)
    }

    override fun maxPermissionSize(uuid: UUID, permission: Permission): Int? {
        return permissionSizes(uuid, permission).maxOrNull()
    }

    override fun minPermissionSize(uuid: UUID, permission: Permission): Int? {
        return permissionSizes(uuid, permission).minOrNull()
    }

    override fun permissionSizes(uuid: UUID, permission: Permission): List<Int> {
        val player = uuid.toOfflinePlayer ?: return emptyList()
        return player.effectivePermissions
            .filter { it.permission.startsWith(permission.value) }
            .map { it.permission }
            .map { it.replace("${permission.value}.", "") }
            .mapNotNull { it.toIntOrNull() }
    }
}

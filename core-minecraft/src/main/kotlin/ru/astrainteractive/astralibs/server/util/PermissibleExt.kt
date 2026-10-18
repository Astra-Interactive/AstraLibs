package ru.astrainteractive.astralibs.server.util

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.util.Tristate
import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
import ru.astrainteractive.astralibs.server.permission.api.Permission
import ru.astrainteractive.klibs.mikro.core.logging.JUtiltLogger
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import java.util.UUID

private const val DEFAULT_OP_LEVEL = 4

/** Lazily checks whether the LuckPerms API class is present on the classpath, caching the result. */
private val isLuckPermsLoaded: Boolean by lazy {
    runCatching { Class.forName("net.luckperms.api.LuckPerms") }.isSuccess
}

private fun luckPermsOrNull(): LuckPerms? {
    return runCatching { LuckPermsProvider.get() }.getOrNull()
}

private fun luckPermsPermissible(uuid: UUID): KPermissible = object : KPermissible {
    override fun hasPermission(permission: Permission): Boolean {
        val tristate = luckPermsOrNull()?.userManager
            ?.getUser(uuid)
            ?.cachedData
            ?.permissionData
            ?.checkPermission(permission.value)
        return tristate == Tristate.TRUE
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        return luckPermsOrNull()?.userManager
            ?.getUser(uuid)
            ?.nodes
            ?.filter { node -> node.key.startsWith(permission.value) }
            ?.filter { node -> !node.hasExpired() }
            ?.map { node -> node.key }
            ?.map { key -> key.replace("${permission.value}.", "") }
            ?.mapNotNull { size -> size.toIntOrNull() }
            .orEmpty()
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).maxOrNull()
    }

    override fun minPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).minOrNull()
    }
}

private fun opLevelPermissible(player: Player): KPermissible = object :
    KPermissible,
    Logger by JUtiltLogger("OpLevelKPermissible") {
    override fun hasPermission(permission: Permission): Boolean {
        error { "#hasPermission could not load permission provider! Use LuckPerms!" }
        return player.hasPermissions(DEFAULT_OP_LEVEL)
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        error { "#hasPermission could not load permission provider! Use LuckPerms!" }
        return emptyList()
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        error { "#hasPermission could not load permission provider! Use LuckPerms!" }
        return null
    }

    override fun minPermissionSize(permission: Permission): Int? {
        error { "#hasPermission could not load permission provider! Use LuckPerms!" }
        return null
    }
}

/**
 * Returns a [KPermissible] backed by LuckPerms for this [Player], or by the vanilla operator level when
 * LuckPerms is not loaded.
 */
public fun Player.asPermissible(): KPermissible {
    return if (isLuckPermsLoaded) {
        luckPermsPermissible(this.uuid)
    } else {
        opLevelPermissible(this)
    }
}

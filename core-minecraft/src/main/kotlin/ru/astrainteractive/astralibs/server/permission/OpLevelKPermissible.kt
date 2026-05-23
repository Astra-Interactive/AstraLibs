package ru.astrainteractive.astralibs.server.permission

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.klibs.mikro.core.logging.JUtiltLogger
import ru.astrainteractive.klibs.mikro.core.logging.Logger

class OpLevelKPermissible(
    private val player: Player
) : KPermissible, Logger by JUtiltLogger("OpLevelKPermissible") {

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

    private companion object {
        private const val DEFAULT_OP_LEVEL = 4
    }
}

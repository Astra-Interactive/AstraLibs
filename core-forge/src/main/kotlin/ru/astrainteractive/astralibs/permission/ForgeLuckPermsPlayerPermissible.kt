package ru.astrainteractive.astralibs.permission

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.util.Tristate
import ru.astrainteractive.astralibs.logging.JUtiltLogger
import ru.astrainteractive.astralibs.logging.Logger
import java.util.UUID

internal class ForgeLuckPermsPlayerPermissible(
    private val uuid: UUID
) : Permissible,
    Logger by JUtiltLogger("AspeKt-ForgeLuckPermsPlayerPermissible") {
    private val luckPermsOrNull: LuckPerms?
        get() = runCatching { LuckPermsProvider.get() }.getOrNull()

    override fun hasPermission(permission: Permission): Boolean {
        val tristate = luckPermsOrNull?.userManager
            ?.getUser(uuid)
            ?.cachedData
            ?.permissionData
            ?.checkPermission(permission.value)
        return tristate == Tristate.TRUE
    }

    override fun maxPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).maxOrNull()
    }

    override fun minPermissionSize(permission: Permission): Int? {
        return permissionSizes(permission).minOrNull()
    }

    override fun permissionSizes(permission: Permission): List<Int> {
        return luckPermsOrNull?.userManager
            ?.getUser(uuid)
            ?.nodes
            ?.filter { it.key.startsWith(permission.value) }
            ?.filter { !it.hasExpired() }
            ?.map { it.key }
            ?.map { it.replace("${permission.value}.", "") }
            ?.mapNotNull { it.toIntOrNull() }
            .orEmpty()
    }
}

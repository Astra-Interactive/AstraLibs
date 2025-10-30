package ru.astrainteractive.astralibs.command.api.context

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.command.api.exception.BadArgumentException
import ru.astrainteractive.astralibs.command.api.exception.NoPermissionException
import ru.astrainteractive.astralibs.permission.Permission

object BukkitCommandContextExt {
    fun BukkitCommandContext.requirePermission(permission: Permission) {
        if (sender.hasPermission(permission.value)) return
        throw NoPermissionException(permission)
    }

    fun <T : Any> BukkitCommandContext.requireArgument(index: Int, type: ArgumentConverter<T>): T {
        val raw = args.getOrNull(index)
        return raw?.let(type::transform) ?: throw BadArgumentException(raw, type)
    }

    fun <T : Any> BukkitCommandContext.findArgument(index: Int, type: ArgumentConverter<T>): T? {
        val raw = args.getOrNull(index)
        return runCatching { raw?.let(type::transform) }.getOrNull()
    }

    fun <T : Any> BukkitCommandContext.argumentOrElse(index: Int, type: ArgumentConverter<T>, default: () -> T): T {
        return findArgument(index, type) ?: default.invoke()
    }
}

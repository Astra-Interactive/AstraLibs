package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.LuckPermsKPermissible

/** Lazily checks whether the LuckPerms API class is present on the classpath, caching the result. */
private val isLuckPermsLoaded: Boolean by lazy {
    runCatching { Class.forName("net.luckperms.api.LuckPerms") }.isSuccess
}

/**
 * Returns a [KPermissible] backed by LuckPerms for this [Player].
 *
 * @throws IllegalStateException if LuckPerms is not loaded on the classpath.
 */
fun Player.asPermissible(): KPermissible {
    return if (isLuckPermsLoaded) {
        LuckPermsKPermissible(this.uuid)
    } else {
        error("No permission provider loaded!")
    }
}

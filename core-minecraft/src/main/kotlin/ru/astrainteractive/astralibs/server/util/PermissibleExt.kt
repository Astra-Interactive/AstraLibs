package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.LuckPermsKPermissible

private val isLuckPermsLoaded: Boolean by lazy {
    runCatching { Class.forName("net.luckperms.api.LuckPerms") }.isSuccess
}

fun Player.asPermissible(): KPermissible {
    return if (isLuckPermsLoaded) {
        LuckPermsKPermissible(this.uuid)
    } else {
        error("No permission provider loaded!")
    }
}

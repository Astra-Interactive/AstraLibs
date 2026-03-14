package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.LuckPermsKPermissible

fun Player.asPermissible(): KPermissible {
    return if (NeoForgeUtil.isModLoaded("luckperms")) {
        LuckPermsKPermissible(this.uuid)
    } else {
        error("No permission provider loaded!")
    }
}

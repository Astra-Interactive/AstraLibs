package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.permission.LuckPermsPermissible
import ru.astrainteractive.astralibs.permission.Permissible

fun Player.asPermissible(): Permissible {
    return if (NeoForgeUtil.isModLoaded("luckperms")) {
        LuckPermsPermissible(this.uuid)
    } else {
        error("No permission provider loaded!")
    }
}

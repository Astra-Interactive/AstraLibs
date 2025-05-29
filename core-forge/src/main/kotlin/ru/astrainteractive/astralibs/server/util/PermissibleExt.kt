package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.player.Player
import ru.astrainteractive.astralibs.permission.ForgeLuckPermsPlayerPermissible
import ru.astrainteractive.astralibs.permission.Permissible

fun Player.asPermissible(): Permissible {
    return if (ForgeUtil.isModLoaded("luckperms")) {
        ForgeLuckPermsPlayerPermissible(this.uuid)
    } else {
        error("No permission provider loaded!")
    }
}

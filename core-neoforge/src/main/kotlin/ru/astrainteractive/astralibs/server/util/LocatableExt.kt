package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.klibs.mikro.core.util.cast

fun OnlineMinecraftPlayerSnapshot.asLocatable(): Locatable {
    val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: error("$this is not online")
    return player.asLocatable()
}

fun Entity.asLocatable() = Locatable {
    KLocation(
        x = this.x,
        y = this.y,
        z = this.z,
        worldName = (level().levelData as ServerLevelData).levelName
    )
}

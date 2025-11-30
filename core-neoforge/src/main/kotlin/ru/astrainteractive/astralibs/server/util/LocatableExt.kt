package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.Location
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.klibs.mikro.core.util.cast

fun OnlineMinecraftPlayer.asLocatable() = Locatable {
    val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: error("$this is not online")
    Location(
        x = player.x,
        y = player.y,
        z = player.z,
        worldName = player.level().levelData.cast<ServerLevelData>().levelName
    )
}

fun Entity.asLocatable() = Locatable {
    Location(
        x = this.x,
        y = this.y,
        z = this.z,
        worldName = (level().levelData as ServerLevelData).levelName
    )
}

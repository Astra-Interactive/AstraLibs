package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.location.KLocation

/** Adapts this [Entity] as a [Locatable] using its current position and world name. */
public fun Entity.asLocatable(): Locatable = Locatable {
    KLocation(
        x = this.x,
        y = this.y,
        z = this.z,
        worldName = (level().levelData as ServerLevelData).levelName
    )
}

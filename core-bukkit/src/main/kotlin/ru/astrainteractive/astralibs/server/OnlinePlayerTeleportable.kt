package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.asTeleportable

class OnlinePlayerTeleportable(private val instance: OnlineMinecraftPlayerSnapshot) : Teleportable {
    override fun teleport(KLocation: ru.astrainteractive.astralibs.server.location.KLocation) {
        instance.asTeleportable().teleport(KLocation)
    }

    @AutoService(Teleportable.Factory::class)
    class Factory : Teleportable.Factory<OnlineMinecraftPlayerSnapshot> {
        override fun from(instance: OnlineMinecraftPlayerSnapshot): Teleportable {
            return OnlinePlayerTeleportable(instance)
        }
    }
}

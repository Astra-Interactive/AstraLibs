package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.asLocatable

class OnlinePlayerLocatable(private val instance: OnlineMinecraftPlayerSnapshot) : Locatable {
    override fun getLocation(): KLocation {
        return instance.asLocatable().getLocation()
    }

    @AutoService(Locatable.Factory::class)
    class Factory : Locatable.Factory<OnlineMinecraftPlayerSnapshot> {
        override fun from(instance: OnlineMinecraftPlayerSnapshot): Locatable {
            return OnlinePlayerLocatable(instance)
        }
    }
}

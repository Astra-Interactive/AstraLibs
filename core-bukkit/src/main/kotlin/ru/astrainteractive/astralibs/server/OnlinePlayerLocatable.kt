package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.location.Location
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asLocatable

class OnlinePlayerLocatable(private val instance: OnlineMinecraftPlayer) : Locatable {
    override fun getLocation(): Location {
        return instance.asLocatable().getLocation()
    }

    @AutoService(Locatable.Factory::class)
    class Factory : Locatable.Factory<OnlineMinecraftPlayer> {
        override fun from(instance: OnlineMinecraftPlayer): Locatable {
            return OnlinePlayerLocatable(instance)
        }
    }
}

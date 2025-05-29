package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import org.bukkit.Bukkit
import org.bukkit.Location
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asTeleportable

class OnlinePlayerTeleportable(private val instance: OnlineMinecraftPlayer) : Teleportable {
    override fun teleport(location: ru.astrainteractive.astralibs.server.location.Location) {
        instance.asTeleportable().teleport(location)
    }

    @AutoService(Teleportable.Factory::class)
    class Factory : Teleportable.Factory<OnlineMinecraftPlayer> {
        override fun from(instance: OnlineMinecraftPlayer): Teleportable {
            return OnlinePlayerTeleportable(instance)
        }
    }
}

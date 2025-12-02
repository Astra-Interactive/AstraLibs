package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asAudience

class OnlinePlayerAudience(private val instance: OnlineMinecraftPlayer) : Audience {
    override fun sendMessage(component: Component) {
        instance.asAudience().sendMessage(component)
    }

    @AutoService(Audience.Factory::class)
    class Factory : Audience.Factory<OnlineMinecraftPlayer> {
        override fun from(instance: OnlineMinecraftPlayer): Audience {
            return OnlinePlayerAudience(instance)
        }
    }
}

package ru.astrainteractive.astralibs.server

import com.google.auto.service.AutoService
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.asAudience

class OnlinePlayerAudience(private val instance: OnlineMinecraftPlayerSnapshot) : Audience {
    override fun sendMessage(component: Component) {
        instance.asAudience().sendMessage(component)
    }

    @AutoService(Audience.Factory::class)
    class Factory : Audience.Factory<OnlineMinecraftPlayerSnapshot> {
        override fun from(instance: OnlineMinecraftPlayerSnapshot): Audience {
            return OnlinePlayerAudience(instance)
        }
    }
}

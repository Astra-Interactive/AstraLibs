package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component
import net.minecraft.server.MinecraftServer
import ru.astrainteractive.astralibs.server.util.toNative

class MinecraftServerKAudience(val server: MinecraftServer) : KAudience {
    override fun sendMessage(component: Component) {
        server.sendSystemMessage(component.toNative())
    }
}

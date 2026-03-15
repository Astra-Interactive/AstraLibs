package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.server.util.toNative

class RconConsoleSourceKAudience(val server: RconConsoleSource) : KAudience {
    override fun sendMessage(component: Component) {
        server.sendSystemMessage(component.toNative())
    }
}

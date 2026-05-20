package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component

/** Platform-agnostic chat-message receiver. Bridges Bukkit/NeoForge audience APIs. */
fun interface KAudience {
    fun sendMessage(component: Component)
}

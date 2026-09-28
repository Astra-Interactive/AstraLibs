package ru.astrainteractive.astralibs.server

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.text.Component

/** An Adventure audience with fixed [pointers] that records every message it receives. */
internal class FakeAudience(private val pointers: Pointers) : Audience {
    val messages = mutableListOf<Component>()

    override fun pointers(): Pointers = pointers

    override fun sendMessage(message: Component) {
        messages += message
    }
}

package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.util.Locale

/** Records every message it receives as plain text. */
internal class FakeReceiver(override val locale: Locale) : KAudience {
    val messages = mutableListOf<String>()

    override fun sendMessage(component: Component) {
        messages += PlainTextComponentSerializer.plainText().serialize(component)
    }
}

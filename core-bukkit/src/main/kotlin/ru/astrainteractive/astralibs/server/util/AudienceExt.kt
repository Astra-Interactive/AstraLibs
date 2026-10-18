package ru.astrainteractive.astralibs.server.util

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.server.api.KAudience
import java.util.Locale

/**
 * Adapts this [Audience], e.g. a player or a command sender, as a receiver that knows its language: the one it
 * reports through [Identity.LOCALE], and none for the console.
 */
public fun Audience.asKAudience(): KAudience {
    val audience = this
    return object : KAudience {
        override val locale: Locale
            get() = audience.get(Identity.LOCALE).orElse(Locale.ROOT)

        override fun sendMessage(component: Component) {
            audience.sendMessage(component)
        }
    }
}

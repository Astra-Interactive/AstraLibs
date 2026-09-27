package ru.astrainteractive.astralibs.server

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import java.util.Locale

/** An Adventure [Audience] with the language it reports through [Identity.LOCALE]; the console reports none. */
class AdventureKAudience(
    private val audience: Audience
) : KAudience {
    override val locale: Locale
        get() = audience.get(Identity.LOCALE).orElse(Locale.ROOT)

    override fun sendMessage(component: Component) {
        audience.sendMessage(component)
    }
}

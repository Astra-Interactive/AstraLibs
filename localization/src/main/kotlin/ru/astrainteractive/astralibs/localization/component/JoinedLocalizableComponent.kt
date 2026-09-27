package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import java.util.Locale

/** Keeps both parts as siblings, so the style of [first] does not leak into [second]. */
internal class JoinedLocalizableComponent(
    private val first: LocalizableComponent,
    private val second: LocalizableComponent
) : LocalizableComponent {
    override fun toComponent(locale: Locale): Component {
        return Component.textOfChildren(first.toComponent(locale), second.toComponent(locale))
    }
}

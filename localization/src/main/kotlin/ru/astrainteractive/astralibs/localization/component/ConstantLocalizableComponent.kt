package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import java.util.Locale

/** A ready [component] that looks the same in every language. */
class ConstantLocalizableComponent(
    private val component: Component
) : LocalizableComponent {
    override fun toComponent(locale: Locale): Component = component
}

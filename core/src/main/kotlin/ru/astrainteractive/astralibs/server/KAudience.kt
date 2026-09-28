package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.localization.component.LocalizableComponent

/**
 * Platform-agnostic chat-message receiver. Bridges Bukkit/NeoForge audience APIs.
 *
 * Every receiver states its language, so a localizable message can be sent to it directly; the console states
 * [java.util.Locale.ROOT] and so reads the default language of each text.
 */
interface KAudience : KLocaleOwner {
    fun sendMessage(component: Component)

    /** Sends [message] in this receiver's [locale]. */
    fun sendMessage(message: LocalizableComponent) {
        sendMessage(message.toComponent(locale))
    }
}

/** Renders [message] once per receiver, so each one reads it in its own language. */
fun Iterable<KAudience>.sendMessage(message: LocalizableComponent) {
    forEach { audience -> audience.sendMessage(message) }
}

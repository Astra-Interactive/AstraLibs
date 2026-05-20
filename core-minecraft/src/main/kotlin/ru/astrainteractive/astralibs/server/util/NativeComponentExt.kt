package ru.astrainteractive.astralibs.server.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.Serializer
import ru.astrainteractive.astralibs.kyori.KyoriComponentSerializer

/**
 * Converts this Adventure [net.kyori.adventure.text.Component] to a vanilla [Component] via JSON.
 * Returns [Component.empty] when the JSON round-trip produces an unparseable result.
 *
 * @see [toKyori]
 */
fun net.kyori.adventure.text.Component.toNative(): Component {
    val json = KyoriComponentSerializer.Json
    val jsonComponent = json.serializer.serialize(this)

    return Serializer.fromJson(jsonComponent) ?: Component.empty()
}

/**
 * Converts this vanilla [Component] to an Adventure [net.kyori.adventure.text.Component] via JSON.
 *
 * @see [toNative]
 */
fun Component.toKyori(): net.kyori.adventure.text.Component {
    val json = Serializer.toJson(this)
    return KyoriComponentSerializer.Json.serializer.deserialize(json)
}

/** Serializes this Adventure component to plain text, stripping all formatting. */
fun net.kyori.adventure.text.Component.toPlain(): String {
    return KyoriComponentSerializer.Plain.serializer.serialize(this)
}

/** Serializes this vanilla [Component] to plain text via [toKyori]. */
fun Component.toPlain(): String {
    return toKyori().toPlain()
}

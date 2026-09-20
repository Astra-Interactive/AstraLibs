package ru.astrainteractive.astralibs.server.util

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import ru.astrainteractive.astralibs.kyori.KyoriComponentSerializer

/**
 * Registries a component may resolve entries against.
 *
 * Falls back to [RegistryAccess.EMPTY] before a server exists: text and formatting survive, only the
 * parts that look up registry entries degrade.
 */
private fun registryProvider(): HolderLookup.Provider {
    return MinecraftUtil.serverOrNull
        ?.registryAccess()
        ?: RegistryAccess.EMPTY
}

/**
 * Converts this Adventure [net.kyori.adventure.text.Component] to a vanilla [Component] via JSON.
 * Returns [Component.empty] when the JSON round-trip produces an unparseable result.
 *
 * @see [toKyori]
 */
fun net.kyori.adventure.text.Component.toNative(): Component {
    val json = KyoriComponentSerializer.Json
    val jsonComponent = json.serializer.serialize(this)

    return Component.Serializer.fromJson(jsonComponent, registryProvider()) ?: Component.empty()
}

/**
 * Converts this vanilla [Component] to an Adventure [net.kyori.adventure.text.Component] via JSON.
 *
 * @see [toNative]
 */
fun Component.toKyori(): net.kyori.adventure.text.Component {
    val json = Component.Serializer.toJson(this, registryProvider())
    return KyoriComponentSerializer.Json.serializer.deserialize(json)
}

fun net.kyori.adventure.text.Component.toPlain(): String {
    return KyoriComponentSerializer.Plain.serializer.serialize(this)
}

fun Component.toPlain(): String {
    return toKyori().toPlain()
}

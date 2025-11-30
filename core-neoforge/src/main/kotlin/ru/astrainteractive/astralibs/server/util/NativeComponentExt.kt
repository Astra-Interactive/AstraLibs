package ru.astrainteractive.astralibs.server.util

import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.Serializer
import ru.astrainteractive.astralibs.kyori.KyoriComponentSerializer

private fun requireHolderLookup(): RegistryAccess.Frozen {
    return NeoForgeUtil.serverOrNull
        ?.registryAccess()
        ?: error("Server is not running")
}

fun net.kyori.adventure.text.Component.toNative(): Component {
    val json = KyoriComponentSerializer.Json
    val jsonComponent = json.serializer.serialize(this)

    return Serializer.fromJson(jsonComponent, requireHolderLookup()) ?: Component.empty()
}

fun Component.toKyori(): net.kyori.adventure.text.Component {
    val json = Serializer.toJson(this, requireHolderLookup())
    return KyoriComponentSerializer.Json.serializer.deserialize(json)
}

fun net.kyori.adventure.text.Component.toPlain(): String {
    return KyoriComponentSerializer.Plain.serializer.serialize(this)
}

fun Component.toPlain(): String {
    return toKyori().toPlain()
}

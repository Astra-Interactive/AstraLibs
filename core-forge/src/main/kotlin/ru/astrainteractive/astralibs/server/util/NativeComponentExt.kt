package ru.astrainteractive.astralibs.server.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.Serializer
import ru.astrainteractive.astralibs.kyori.KyoriComponentSerializer

fun net.kyori.adventure.text.Component.toNative(): Component {
    val json = KyoriComponentSerializer.Json
    val jsonComponent = json.serializer.serialize(this)
    return Serializer.fromJson(jsonComponent) ?: Component.empty()
}

fun Component.toKyori(): net.kyori.adventure.text.Component {
    val json = Serializer.toJson(this)
    return KyoriComponentSerializer.Json.serializer.deserialize(json)
}

fun net.kyori.adventure.text.Component.toPlain(): String {
    return KyoriComponentSerializer.Plain.serializer.serialize(this)
}

fun Component.toPlain(): String {
    return toKyori().toPlain()
}

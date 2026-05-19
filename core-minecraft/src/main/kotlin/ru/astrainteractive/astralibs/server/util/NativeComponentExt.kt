package ru.astrainteractive.astralibs.server.util

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.RegistryOps
import ru.astrainteractive.astralibs.kyori.KyoriComponentSerializer

fun net.kyori.adventure.text.Component.toNative(): Component {
    val jsonString = KyoriComponentSerializer.Json.serializer.serialize(this)
    val jsonElement = JsonParser.parseString(jsonString)
    val ops = RegistryOps.create(JsonOps.INSTANCE, MinecraftUtil.requireServer().registryAccess())
    return ComponentSerialization.CODEC
        .parse(ops, jsonElement)
        .resultOrPartial { }
        .orElse(Component.empty())
        ?: Component.empty()
}

fun Component.toKyori(): net.kyori.adventure.text.Component {
    val ops = RegistryOps.create(JsonOps.INSTANCE, MinecraftUtil.requireServer().registryAccess())
    val jsonString = ComponentSerialization.CODEC
        .encodeStart(ops, this)
        .resultOrPartial { }
        .map { it.toString() }
        .orElse(null)
        ?: return net.kyori.adventure.text.Component.empty()
    return KyoriComponentSerializer.Json.serializer.deserialize(jsonString)
}

fun net.kyori.adventure.text.Component.toPlain(): String {
    return KyoriComponentSerializer.Plain.serializer.serialize(this)
}

fun Component.toPlain(): String {
    return toKyori().toPlain()
}



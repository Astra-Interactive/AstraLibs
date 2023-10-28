package ru.astrainteractive.astralibs.serialization

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.string.StringDesc

/**
 * [KyoriComponentSerializer] is a wrapper for [net.kyori.adventure] serializer
 */
sealed class KyoriComponentSerializer(val type: KyoriComponentSerializerType) {

    abstract fun toComponent(string: String): Component

    fun toComponent(stringDesc: StringDesc): Component {
        return when (stringDesc) {
            is StringDesc.Raw -> toComponent(stringDesc.raw)
        }
    }

    data object Json : KyoriComponentSerializer(KyoriComponentSerializerType.Json) {
        override fun toComponent(string: String): Component {
            return JSONComponentSerializer.json().deserialize(string)
        }
    }

    data object Gson : KyoriComponentSerializer(KyoriComponentSerializerType.Gson) {
        override fun toComponent(string: String): Component {
            return GsonComponentSerializer.gson().deserialize(string)
        }
    }

    data object Plain : KyoriComponentSerializer(KyoriComponentSerializerType.Plain) {
        override fun toComponent(string: String): Component {
            return PlainTextComponentSerializer.plainText().deserialize(string)
        }
    }

    data object MiniMessage : KyoriComponentSerializer(KyoriComponentSerializerType.MiniMessage) {
        override fun toComponent(string: String): Component {
            return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(string)
        }
    }

    data object Legacy : KyoriComponentSerializer(KyoriComponentSerializerType.Legacy) {
        override fun toComponent(string: String): Component {
            return LegacyComponentSerializer
                .legacy(LegacyComponentSerializer.AMPERSAND_CHAR)
                .deserialize(string)
                .decoration(TextDecoration.ITALIC, false)
        }
    }

    companion object {
        fun ofType(type: KyoriComponentSerializerType) = when (type) {
            KyoriComponentSerializerType.Json -> Json
            KyoriComponentSerializerType.Gson -> Gson
            KyoriComponentSerializerType.Plain -> Plain
            KyoriComponentSerializerType.MiniMessage -> MiniMessage
            KyoriComponentSerializerType.Legacy -> Legacy
        }
    }
}

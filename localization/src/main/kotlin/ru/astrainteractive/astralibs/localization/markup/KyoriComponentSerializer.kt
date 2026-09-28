package ru.astrainteractive.astralibs.localization.markup

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.localization.component.withItalicOffByDefault

/** Converts strings to Kyori Adventure [Component]s. */
interface KyoriComponentSerializer {

    val type: KyoriComponentSerializerType
    val serializer: ComponentSerializer<Component, out Component, String>

    fun toComponent(string: String): Component

    /** JSON component format using [JSONComponentSerializer]. */
    data object Json : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Json
        override val serializer by lazy {
            JSONComponentSerializer.json()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Gson JSON component format using [GsonComponentSerializer]. */
    data object Gson : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Gson
        override val serializer by lazy {
            GsonComponentSerializer.gson()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Plain text — no colors or formatting tags. */
    data object Plain : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Plain
        override val serializer by lazy {
            PlainTextComponentSerializer.plainText()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** MiniMessage format. */
    data object MiniMessage : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.MiniMessage
        override val serializer by lazy {
            net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Legacy `&`-color-code format; italic decoration is disabled by default. */
    data object Legacy : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Legacy
        override val serializer by lazy {
            LegacyComponentSerializer.builder()
                .extractUrls()
                .character(LegacyComponentSerializer.AMPERSAND_CHAR)
                .build()
        }

        override fun toComponent(string: String): Component {
            return serializer
                .deserialize(string)
                .withItalicOffByDefault()
        }
    }

    companion object {
        fun ofType(type: KyoriComponentSerializerType) = when (type) {
            KyoriComponentSerializerType.Json -> Json
            KyoriComponentSerializerType.Gson -> Gson
            KyoriComponentSerializerType.Plain -> Plain
            KyoriComponentSerializerType.MiniMessage -> MiniMessage
            KyoriComponentSerializerType.Legacy -> Legacy
            KyoriComponentSerializerType.Auto -> AutoComponentSerializer
        }
    }
}

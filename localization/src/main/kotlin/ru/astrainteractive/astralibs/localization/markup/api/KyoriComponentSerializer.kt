package ru.astrainteractive.astralibs.localization.markup.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.localization.markup.model.KyoriComponentSerializerType

/** Item names and lore render italic unless told otherwise, while config text is meant to be upright. */
internal fun withItalicOffByDefault(component: Component): Component {
    return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

/** Converts strings to Kyori Adventure [Component]s. */
public interface KyoriComponentSerializer {

    public val type: KyoriComponentSerializerType
    public val serializer: ComponentSerializer<Component, out Component, String>

    public fun toComponent(string: String): Component

    /** JSON component format using [JSONComponentSerializer]. */
    public data object Json : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Json
        override val serializer: JSONComponentSerializer by lazy {
            JSONComponentSerializer.json()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Gson JSON component format using [GsonComponentSerializer]. */
    public data object Gson : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Gson
        override val serializer: GsonComponentSerializer by lazy {
            GsonComponentSerializer.gson()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Plain text — no colors or formatting tags. */
    public data object Plain : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Plain
        override val serializer: PlainTextComponentSerializer by lazy {
            PlainTextComponentSerializer.plainText()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** MiniMessage format. */
    public data object MiniMessage : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.MiniMessage
        override val serializer: net.kyori.adventure.text.minimessage.MiniMessage by lazy {
            net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /** Legacy `&`-color-code format; italic decoration is disabled by default. */
    public data object Legacy : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Legacy
        override val serializer: LegacyComponentSerializer by lazy {
            LegacyComponentSerializer.builder()
                .extractUrls()
                .character(LegacyComponentSerializer.AMPERSAND_CHAR)
                .build()
        }

        override fun toComponent(string: String): Component {
            return withItalicOffByDefault(serializer.deserialize(string))
        }
    }

    public companion object {
        public fun ofType(type: KyoriComponentSerializerType): KyoriComponentSerializer = when (type) {
            KyoriComponentSerializerType.Json -> Json
            KyoriComponentSerializerType.Gson -> Gson
            KyoriComponentSerializerType.Plain -> Plain
            KyoriComponentSerializerType.MiniMessage -> MiniMessage
            KyoriComponentSerializerType.Legacy -> Legacy
            KyoriComponentSerializerType.Auto -> AutoComponentSerializer
        }
    }
}

package ru.astrainteractive.astralibs.kyori

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.string.StringDesc

/**
 * Interface representing a serializer for [Component]s from Kyori Adventure,
 * allowing parsing from strings to component objects using various formats.
 */
interface KyoriComponentSerializer {

    /**
     * The type identifier for this serializer.
     */
    val type: KyoriComponentSerializerType

    /**
     * The actual Kyori serializer instance used to parse and deserialize strings.
     */
    val serializer: ComponentSerializer<Component, out Component, String>

    /**
     * Converts a raw string into a Kyori [Component].
     *
     * @param string The input string.
     * @return A deserialized [Component].
     */
    fun toComponent(string: String): Component

    /**
     * Converts a [StringDesc] into a Kyori [Component].
     *
     * Supports both raw and plain string descriptors.
     *
     * @param stringDesc The string descriptor.
     * @return A deserialized [Component].
     */
    fun toComponent(stringDesc: StringDesc): Component {
        return when (stringDesc) {
            is StringDesc.Raw -> toComponent(stringDesc.raw)
            is StringDesc.Plain -> toComponent(stringDesc.raw)
        }
    }

    /**
     * Extension property to convert a [StringDesc] directly into a [Component].
     */
    val StringDesc.component get() = toComponent(this)

    /**
     * JSON-based serializer using Kyori's [JSONComponentSerializer].
     */
    data object Json : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Json
        override val serializer by lazy {
            JSONComponentSerializer.json()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /**
     * Gson-based serializer using Kyori's [GsonComponentSerializer].
     */
    data object Gson : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Gson
        override val serializer by lazy {
            GsonComponentSerializer.gson()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /**
     * Plain text serializer using Kyori's [PlainTextComponentSerializer].
     */
    data object Plain : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Plain
        override val serializer by lazy {
            PlainTextComponentSerializer.plainText()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /**
     * MiniMessage serializer using Kyori's [MiniMessage] DSL.
     */
    data object MiniMessage : KyoriComponentSerializer {
        override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.MiniMessage
        override val serializer by lazy {
            net.kyori.adventure.text.minimessage.MiniMessage.miniMessage()
        }

        override fun toComponent(string: String): Component {
            return serializer.deserialize(string)
        }
    }

    /**
     * Legacy serializer using Kyori's [LegacyComponentSerializer] with ampersand color codes.
     * Also disables italic decoration by default.
     */
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
                .decoration(TextDecoration.ITALIC, false)
        }
    }

    companion object {
        /**
         * Returns the [KyoriComponentSerializer] corresponding to the specified [type].
         *
         * @param type The serializer type.
         * @return The corresponding serializer instance.
         */
        fun ofType(type: KyoriComponentSerializerType) = when (type) {
            KyoriComponentSerializerType.Json -> Json
            KyoriComponentSerializerType.Gson -> Gson
            KyoriComponentSerializerType.Plain -> Plain
            KyoriComponentSerializerType.MiniMessage -> MiniMessage
            KyoriComponentSerializerType.Legacy -> Legacy
        }
    }
}

package ru.astrainteractive.astralibs.kyori

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

/**
 * Enumeration of supported Kyori component serializer types.
 *
 * Each type corresponds to a specific format for serializing and deserializing [Component]s.
 */
@Serializable
enum class KyoriComponentSerializerType {
    /**
     * JSON format using [JSONComponentSerializer].
     */
    Json,

    /**
     * Gson format using [GsonComponentSerializer].
     */
    Gson,

    /**
     * Plain text format using [PlainTextComponentSerializer].
     */
    Plain,

    /**
     * MiniMessage format using Kyori's [KyoriComponentSerializer.MiniMessage] DSL.
     */
    MiniMessage,

    /**
     * Legacy format using color codes (e.g., '&') via [LegacyComponentSerializer].
     */
    Legacy
}

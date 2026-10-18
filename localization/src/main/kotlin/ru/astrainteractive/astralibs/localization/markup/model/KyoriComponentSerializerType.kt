package ru.astrainteractive.astralibs.localization.markup.model

import kotlinx.serialization.Serializable

/** Supported [ru.astrainteractive.astralibs.localization.markup.api.KyoriComponentSerializer] formats. */
@Serializable
public enum class KyoriComponentSerializerType {
    /** JSON component format. */
    Json,

    /** Gson-serialized JSON component format. */
    Gson,

    /** Plain text — no colors or formatting tags. */
    Plain,

    /** MiniMessage component format. */
    MiniMessage,

    /** Legacy color-code format (e.g. `'&'`). */
    Legacy,

    /**
     * MiniMessage tags and legacy `&` codes in one string, see
     * [ru.astrainteractive.astralibs.localization.markup.api.AutoComponentSerializer].
     */
    Auto
}

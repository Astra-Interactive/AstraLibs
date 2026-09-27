package ru.astrainteractive.astralibs.localization.markup

import kotlinx.serialization.Serializable

/** Supported [KyoriComponentSerializer] formats. */
@Serializable
enum class KyoriComponentSerializerType {
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

    /** MiniMessage tags and legacy `&` codes in one string, see [AutoComponentSerializer]. */
    Auto
}

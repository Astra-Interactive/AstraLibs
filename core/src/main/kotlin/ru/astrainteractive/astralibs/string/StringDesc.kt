package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable
import ru.astrainteractive.astralibs.serialization.DefaultStringDescSerializer
import ru.astrainteractive.astralibs.serialization.PlainStringDescSerializer
import ru.astrainteractive.astralibs.serialization.RawStringDescSerializer

/** Text description that is either [Raw] (supports color codes) or [Plain] (no formatting). */
@Serializable(with = DefaultStringDescSerializer::class)
sealed interface StringDesc {
    val raw: String

    /** Raw string with color codes (e.g. `&a`, MiniMessage tags). */
    @JvmInline
    @Serializable(with = RawStringDescSerializer::class)
    value class Raw(override val raw: String) : StringDesc

    /** Plain string with no colors or formatting. */
    @JvmInline
    @Serializable(with = PlainStringDescSerializer::class)
    value class Plain(override val raw: String) : StringDesc
}

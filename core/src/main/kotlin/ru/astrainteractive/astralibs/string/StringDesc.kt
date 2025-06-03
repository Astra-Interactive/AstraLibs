package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable

/**
 * Represents a text description that can be either raw (with color codes)
 * or plain (without any special formatting).
 *
 * @property raw The underlying string value of the description.
 */
@Serializable(with = DefaultStringDescSerializer::class)
sealed interface StringDesc {
    val raw: String

    /**
     * Represents a raw text description that supports color codes.
     *
     * Example: `#FF0000[Title] #00FF00This is #0000FFCOLORED #00FF00Text format!`
     *
     * @property raw The raw string containing color codes and text.
     */
    @JvmInline
    @Serializable(with = RawStringDescSerializer::class)
    value class Raw(override val raw: String) : StringDesc

    /**
     * Represents a plain text description without any colors or formatting.
     *
     * Example: `Hello world, this is simple plain text format!`
     *
     * @property raw The plain string value.
     */
    @JvmInline
    @Serializable(with = PlainStringDescSerializer::class)
    value class Plain(override val raw: String) : StringDesc
}

package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable

sealed interface StringDesc {
    val raw: String

    /**
     * Raw is used to convert raw text string into colored components
     * example: #FF0000[Title] #00FF00This is #0000FFCOLORED #00FF00Text format!
     */
    @JvmInline
    @Serializable(with = RawStringDescSerializer::class)
    value class Raw(override val raw: String) : StringDesc

    /**
     * Plain is used for plain text without any colors etc.
     * example: Hello world, this is simple plain text format!
     */
    @JvmInline
    @Serializable(with = PlainStringDescSerializer::class)
    value class Plain(override val raw: String) : StringDesc
}

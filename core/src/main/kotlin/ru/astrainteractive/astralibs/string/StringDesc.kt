package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable

sealed interface StringDesc {
    val raw: String

    @JvmInline
    @Serializable(with = RawStringDescSerializer::class)
    value class Raw(override val raw: String) : StringDesc

    @JvmInline
    @Serializable(with = PlainStringDescSerializer::class)
    value class Plain(override val raw: String) : StringDesc
}

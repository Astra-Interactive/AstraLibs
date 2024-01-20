package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable

sealed interface StringDesc {
    val raw: String

    @JvmInline
    @Serializable(with = StringDescSerializer::class)
    value class Raw(override val raw: String) : StringDesc
}

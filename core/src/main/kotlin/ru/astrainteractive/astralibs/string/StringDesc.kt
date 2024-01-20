package ru.astrainteractive.astralibs.string

import kotlinx.serialization.Serializable

sealed interface StringDesc {
    @JvmInline
    @Serializable(with = StringDescSerializer::class)
    value class Raw(val raw: String) : StringDesc
}

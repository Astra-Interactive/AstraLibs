package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

interface Serializer {
    val serializersModule: kotlinx.serialization.modules.SerializersModule

    /**
     * Convert [string] yaml into [T]
     */
    fun <T> decodeFromString(serializer: DeserializationStrategy<T>, string: String): T

    /**
     * Convert [value] into string serialized value
     */
    fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String
}

package ru.astrainteractive.astralibs.encoding.model

/**
 * Wrapper for IO classes to handle [Encoder] more safely
 */
sealed interface EncodedObject {
    @JvmInline
    value class Base64(val value: String) : EncodedObject

    @JvmInline
    value class ByteArray(val value: kotlin.ByteArray) : EncodedObject
}

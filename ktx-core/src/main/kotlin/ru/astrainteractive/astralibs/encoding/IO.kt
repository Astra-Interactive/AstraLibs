package ru.astrainteractive.astralibs.encoding

/**
 * Wrapper for IO classes to handle [Serializer] more safely
 */
sealed interface IO {
    @JvmInline
    value class Base64(val value: String) : IO

    @JvmInline
    value class ByteArray(val value: kotlin.ByteArray) : IO
}

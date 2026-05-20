package ru.astrainteractive.astralibs.encoding.model

/** A serialized object in one of two forms: raw bytes or Base64 string. */
sealed interface EncodedObject {
    /** Base64-encoded string form. */
    @JvmInline
    value class Base64(val value: String) : EncodedObject

    /** Raw byte array form. */
    @JvmInline
    value class ByteArray(val value: kotlin.ByteArray) : EncodedObject
}

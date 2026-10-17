package ru.astrainteractive.astralibs.encoding.model

/** A serialized object in one of two forms: raw bytes or Base64 string. */
public sealed interface EncodedObject {
    /** Base64-encoded string form. */
    @JvmInline
    public value class Base64(public val value: String) : EncodedObject

    /** Raw byte array form. */
    @JvmInline
    public value class ByteArray(public val value: kotlin.ByteArray) : EncodedObject
}

package ru.astrainteractive.astralibs.encoding.model

/**
 * A sealed interface representing an encoded form of an object.
 *
 * Implementations include encoded data as either a raw byte array or a Base64-encoded string.
 */
sealed interface EncodedObject {

    /**
     * A value class representing a Base64-encoded string form of an object.
     *
     * This format is useful for text-based transmission or storage of serialized objects.
     *
     * @property value The Base64-encoded string.
     */
    @JvmInline
    value class Base64(val value: String) : EncodedObject

    /**
     * A value class representing the raw byte array form of an encoded object.
     *
     * This format is efficient for binary storage or internal transport.
     *
     * @property value The raw byte array.
     */
    @JvmInline
    value class ByteArray(val value: kotlin.ByteArray) : EncodedObject
}

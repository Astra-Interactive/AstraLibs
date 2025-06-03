package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

/**
 * Interface for serializing and deserializing objects to and from different encoded formats.
 *
 * Provides methods for converting objects to a byte array or a Base64-encoded string,
 * and reconstructing them back from these formats.
 */
interface ObjectEncoder {

    /**
     * Serializes the given object [obj] into a raw byte array representation.
     *
     * @param T The type of the object to serialize.
     * @param obj The object to serialize.
     * @return A wrapper containing the byte array representation of the object.
     */
    fun <T> toByteArray(obj: T): EncodedObject.ByteArray

    /**
     * Deserializes the provided byte array into an object of type [T].
     *
     * @param T The type of the resulting object.
     * @param byteArray The byte array to deserialize.
     * @return The deserialized object.
     */
    fun <T> fromByteArray(byteArray: EncodedObject.ByteArray): T

    /**
     * Serializes the given object [obj] into a Base64-encoded string.
     *
     * @param T The type of the object to serialize.
     * @param obj The object to serialize.
     * @return A wrapper containing the Base64 representation of the object.
     */
    fun <T> toBase64(obj: T): EncodedObject.Base64

    /**
     * Deserializes the provided Base64 string into an object of type [T].
     *
     * @param T The type of the resulting object.
     * @param base64 The Base64 string to deserialize.
     * @return The deserialized object.
     */
    fun <T> fromBase64(base64: EncodedObject.Base64): T
}

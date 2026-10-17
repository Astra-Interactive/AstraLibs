package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

/** Serializes and deserializes objects to/from byte arrays and Base64 strings. */
public interface ObjectEncoder {
    public fun <T> toByteArray(obj: T): EncodedObject.ByteArray
    public fun <T> fromByteArray(byteArray: EncodedObject.ByteArray): T
    public fun <T> toBase64(obj: T): EncodedObject.Base64
    public fun <T> fromBase64(base64: EncodedObject.Base64): T
}

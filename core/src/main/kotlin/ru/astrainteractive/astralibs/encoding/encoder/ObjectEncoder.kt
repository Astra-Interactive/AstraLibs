package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

/** Serializes and deserializes objects to/from byte arrays and Base64 strings. */
interface ObjectEncoder {
    fun <T> toByteArray(obj: T): EncodedObject.ByteArray
    fun <T> fromByteArray(byteArray: EncodedObject.ByteArray): T
    fun <T> toBase64(obj: T): EncodedObject.Base64
    fun <T> fromBase64(base64: EncodedObject.Base64): T
}

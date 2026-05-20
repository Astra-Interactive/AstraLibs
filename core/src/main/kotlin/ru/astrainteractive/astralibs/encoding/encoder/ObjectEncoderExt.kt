package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

/** Encodes a list of objects into a Base64 wrapper. */
inline fun <reified T> ObjectEncoder.encodeList(objects: List<T>): EncodedObject.Base64 {
    return toBase64(objects)
}

/** Decodes a Base64 wrapper into a list, returning an empty list if decoding fails. */
inline fun <reified T> ObjectEncoder.decodeList(encoded: EncodedObject.Base64): List<T> {
    return fromBase64(encoded) ?: emptyList()
}

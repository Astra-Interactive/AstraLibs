package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

/**
 * Encodes a list of objects into a Base64-encoded representation.
 *
 * @param T The type of objects in the list.
 * @param objects The list of objects to encode.
 * @return A Base64-encoded wrapper representing the list.
 */
inline fun <reified T> ObjectEncoder.encodeList(objects: List<T>): EncodedObject.Base64 {
    return toBase64(objects)
}

/**
 * Decodes a Base64-encoded representation into a list of objects.
 *
 * If decoding fails or results in `null`, an empty list is returned.
 *
 * @param T The type of objects in the resulting list.
 * @param encoded The Base64-encoded wrapper to decode.
 * @return A list of decoded objects, or an empty list if decoding fails.
 */
inline fun <reified T> ObjectEncoder.decodeList(encoded: EncodedObject.Base64): List<T> {
    return fromBase64(encoded) ?: emptyList()
}

package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.model.EncodedObject

object ObjectEncoderExt {
    inline fun <reified T> ObjectEncoder.encodeList(objects: List<T>): EncodedObject.Base64 {
        return toBase64(objects)
    }

    inline fun <reified T> ObjectEncoder.decodeList(encoded: EncodedObject.Base64): List<T> {
        return fromBase64(encoded) ?: emptyList()
    }
}

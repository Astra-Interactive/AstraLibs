package ru.astrainteractive.astralibs.encoding.model

import ru.astrainteractive.astralibs.encoding.encoder.ObjectEncoder

/**
 * Wrapper for IO classes to handle [ObjectEncoder] more safely
 */
sealed interface EncodedObject {
    @JvmInline
    value class Base64(val value: String) : EncodedObject

    @JvmInline
    value class ByteArray(val value: kotlin.ByteArray) : EncodedObject
}

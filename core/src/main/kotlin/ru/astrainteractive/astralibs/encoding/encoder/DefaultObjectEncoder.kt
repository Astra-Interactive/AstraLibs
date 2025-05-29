package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.factory.JavaObjectStreamFactory
import ru.astrainteractive.astralibs.encoding.factory.ObjectStreamFactory
import ru.astrainteractive.astralibs.encoding.model.EncodedObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Default implementation of the [ObjectEncoder] interface.
 *
 * Uses a provided [ObjectStreamFactory] to handle the actual serialization and
 * deserialization of objects to and from byte arrays and Base64 strings.
 *
 * @property streamProvider The factory responsible for creating object input/output streams.
 * Defaults to [JavaObjectStreamFactory], which uses standard Java serialization.
 */
class DefaultObjectEncoder(
    private val streamProvider: ObjectStreamFactory = JavaObjectStreamFactory,
) : ObjectEncoder {

    override fun <T> toByteArray(obj: T): EncodedObject.ByteArray {
        val bostream = ByteArrayOutputStream()
        val os = streamProvider.createOutputStream(bostream)
        os.writeObject(obj)
        os.flush()
        return EncodedObject.ByteArray(bostream.toByteArray())
    }

    override fun <T> fromByteArray(byteArray: EncodedObject.ByteArray): T {
        val byteArrayInputStream = ByteArrayInputStream(byteArray.value)
        val objectInputStream = streamProvider.createInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as T
    }

    override fun <T> toBase64(obj: T): EncodedObject.Base64 {
        val encoder = java.util.Base64.getEncoder()
        val byteArray = toByteArray(obj)
        val encoded = encoder.encodeToString(byteArray.value)
        return EncodedObject.Base64(encoded)
    }

    override fun <T> fromBase64(base64: EncodedObject.Base64): T {
        val decoder = java.util.Base64.getDecoder()
        val decoded = decoder.decode(base64.value)
        return fromByteArray<T>(EncodedObject.ByteArray(decoded))
    }
}

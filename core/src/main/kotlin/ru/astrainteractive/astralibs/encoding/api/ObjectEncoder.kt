package ru.astrainteractive.astralibs.encoding.api

import ru.astrainteractive.astralibs.encoding.model.EncodedObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/** Serializes and deserializes objects to/from byte arrays and Base64 strings. */
public interface ObjectEncoder {
    public fun <T> toByteArray(obj: T): EncodedObject.ByteArray

    public fun <T> fromByteArray(byteArray: EncodedObject.ByteArray): T

    public fun <T> toBase64(obj: T): EncodedObject.Base64

    public fun <T> fromBase64(base64: EncodedObject.Base64): T

    /**
     * [ObjectEncoder] backed by an [ObjectStreamFactory].
     *
     * @param streamProvider Defaults to [ObjectStreamFactory.Java] (standard Java serialization).
     */
    public class Default(
        private val streamProvider: ObjectStreamFactory = ObjectStreamFactory.Java,
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

    /** [ObjectEncoder] using standard Java object serialization. */
    public class Java : ObjectEncoder by Default(ObjectStreamFactory.Java)
}

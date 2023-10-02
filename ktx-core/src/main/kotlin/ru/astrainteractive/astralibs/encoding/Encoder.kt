package ru.astrainteractive.astralibs.encoding

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Decoder/Encoder for [Serializable] objects
 *
 * Consider use BukkitInputStreamProvider and BukkitOutputStreamProvider
 * from spigot-core if using [Encoder] with bukkit objects
 */
class Encoder(
    private val streamProvider: IOStreamProvider = JavaIOStreamProvider,
) {

    fun <T> toByteArray(obj: T): IO.ByteArray {
        val bostream = ByteArrayOutputStream()
        val os = streamProvider.provideOutputStream(bostream)
        os.writeObject(obj)
        os.flush()
        return IO.ByteArray(bostream.toByteArray())
    }

    fun <T> fromByteArray(byteArray: IO.ByteArray): T {
        val byteArrayInputStream = ByteArrayInputStream(byteArray.value)
        val objectInputStream = streamProvider.provideInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as T
    }

    fun <T> toBase64(obj: T): IO.Base64 {
        val encoder = java.util.Base64.getEncoder()
        val byteArray = toByteArray(obj)
        val encoded = encoder.encodeToString(byteArray.value)
        return IO.Base64(encoded)
    }

    fun <T> fromBase64(base64: IO.Base64): T {
        val decoder = java.util.Base64.getDecoder()
        val decoded = decoder.decode(base64.value)
        return fromByteArray<T>(IO.ByteArray(decoded))
    }

    inline fun <reified T> encodeList(objects: List<T>): IO.Base64 {
        return toBase64(objects)
    }

    inline fun <reified T> decodeList(encoded: IO.Base64): List<T> {
        return fromBase64(encoded) ?: emptyList()
    }
}
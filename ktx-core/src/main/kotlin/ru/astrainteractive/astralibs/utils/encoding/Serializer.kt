package ru.astrainteractive.astralibs.utils.encoding

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Decoder/Encoder for [Serializable] objects
 * Consider use BukkitInputStreamProvider and BukkitOutputStreamProvider from spigot-core if using [Serializer] with bukkit objects
 */
class Serializer(
    private val streamProvider: IOStreamProvider = JavaIOStreamProvider,
) {
    sealed interface Wrapper {
        @JvmInline
        value class Base64(val value: String):Wrapper
        @JvmInline
        value class ByteArray(val value: kotlin.ByteArray):Wrapper

    }

    fun <T> toByteArray(obj: T): Wrapper.ByteArray {
        val bostream = ByteArrayOutputStream()
        val os = streamProvider.provideOutputStream(bostream)
        os.writeObject(obj)
        os.flush()
        return Wrapper.ByteArray(bostream.toByteArray())
    }

    fun <T> fromByteArray(byteArray: Wrapper.ByteArray): T {
        val _in = ByteArrayInputStream(byteArray.value)
        val _is = streamProvider.provideInputStream(_in)
        return _is.readObject() as T
    }

    fun <T> toBase64(obj: T): Wrapper.Base64 {
        val encoder = java.util.Base64.getEncoder()
        val byteArray = toByteArray(obj)
        val encoded = encoder.encodeToString(byteArray.value)
        return Wrapper.Base64(encoded)
    }

    fun <T> fromBase64(base64: Wrapper.Base64): T {
        val decoder = java.util.Base64.getDecoder()
        val decoded = decoder.decode(base64.value)
        return fromByteArray<T>(Wrapper.ByteArray(decoded))
    }

    inline fun <reified T> encodeList(objects: List<T>): Wrapper.Base64 {
        return toBase64(objects)
    }

    inline fun <reified T> decodeList(encoded: Wrapper.Base64): List<T> {
        return fromBase64(encoded) ?: emptyList()
    }
}
package ru.astrainteractive.astralibs.utils.encoding

import ru.astrainteractive.astralibs.utils.catching
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.util.*

/**
 * Decoder/Encoder for [Serializable] objects
 * Consider use BukkitInputStreamProvider and BukkitOutputStreamProvider from spigot-core if using [Serializer] with bukkit objects
 */
object Serializer {
    fun <T> toByteArray(
        obj: T,
        outputStreamProvider: IOutputStreamProvider = DefaultOutputStreamProvider
    ): ByteArray {
        val io = ByteArrayOutputStream()
        val os = outputStreamProvider.provide(io)
        os.writeObject(obj)
        os.flush()
        return io.toByteArray()
    }

    fun <T> fromByteArray(
        byteArray: ByteArray,
        inputStreamProvider: IInputStreamProvider = DefaultInputStreamProvider
    ): T {
        val _in = ByteArrayInputStream(byteArray)
        val _is = inputStreamProvider.provide(_in)
        return _is.readObject() as T
    }

    fun <T> toBase64(
        obj: T,
        outputStreamProvider: IOutputStreamProvider = DefaultOutputStreamProvider
    ): String {
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(toByteArray(obj, outputStreamProvider))
    }

    fun <T> fromBase64(
        string: String,
        inputStreamProvider: IInputStreamProvider = DefaultInputStreamProvider
    ): T {
        val decoder = Base64.getDecoder()
        return fromByteArray<T>(decoder.decode(string), inputStreamProvider)
    }

    inline fun <reified T> encodeList(
        objects: List<T>,
        outputStreamProvider: IOutputStreamProvider = DefaultOutputStreamProvider
    ): String {
        return toBase64(objects, outputStreamProvider)
    }

    inline fun <reified T> decodeList(
        encoded: String,
        inputStreamProvider: IInputStreamProvider = DefaultInputStreamProvider
    ): List<T> {
        return fromBase64(encoded, inputStreamProvider) ?: emptyList()
    }
}
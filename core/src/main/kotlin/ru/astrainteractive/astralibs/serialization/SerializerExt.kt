package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import java.io.File

object SerializerExt {
    /**
     * Inline extension for [Serializer]
     */
    inline fun <reified T> Serializer.encodeToString(value: T): String {
        return encodeToString(serializersModule.serializer<T>(), value)
    }

    /**
     * Inline extension for [Serializer]
     */
    inline fun <reified T> Serializer.decodeFromString(string: String): T {
        return decodeFromString(serializersModule.serializer<T>(), string)
    }

    /**
     * Attempts to parse content of [file] and return [T] as kotlin's result
     */
    fun <T> Serializer.parse(deserializer: DeserializationStrategy<T>, file: File) = kotlin.runCatching {
        decodeFromString(deserializer, file.readText())
    }

    inline fun <reified T> Serializer.parse(file: File): Result<T> {
        return parse(serializersModule.serializer<T>(), file)
    }

    /**
     * Attempt to parse. Returns default [factory] value on failure
     */
    fun <T> Serializer.parseOrDefault(deserializer: DeserializationStrategy<T>, file: File, factory: () -> T): T {
        return parse<T>(deserializer, file).getOrElse { factory.invoke() }
    }

    inline fun <reified T> Serializer.parseOrDefault(file: File, noinline factory: () -> T): T {
        return parseOrDefault(serializersModule.serializer<T>(), file, factory)
    }

    /**
     * Converts [value] into string by [encodeToString] and write text into file [file]
     */
    fun <T> Serializer.writeIntoFile(serializer: SerializationStrategy<T>, value: T, file: File) {
        val string = encodeToString(serializer, value)
        file.writeText(string)
    }

    inline fun <reified T> Serializer.writeIntoFile(value: T, file: File) {
        writeIntoFile(serializersModule.serializer<T>(), value, file)
    }
}

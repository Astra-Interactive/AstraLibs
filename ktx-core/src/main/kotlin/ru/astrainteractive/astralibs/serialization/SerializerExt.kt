package ru.astrainteractive.astralibs.serialization

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
    inline fun <reified T> Serializer.parse(file: File) = kotlin.runCatching {
        decodeFromString<T>(file.readText())
    }

    /**
     * Attempt to parse. Returns default [factory] value on failure
     */
    inline fun <reified T> Serializer.parseOrDefault(file: File, factory: () -> T): T {
        return parse<T>(file).getOrElse { factory.invoke() }
    }

    /**
     * Converts [value] into string by [encodeToString] and write text into file [file]
     */
    inline fun <reified T> Serializer.writeIntoFile(value: T, file: File) {
        val string = encodeToString(value)
        file.writeText(string)
    }
}

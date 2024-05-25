package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import java.io.File

object StringFormatExt {
    /**
     * Attempts to parse content of [file] and return [T] as kotlin's result
     */
    fun <T> StringFormat.parse(deserializer: DeserializationStrategy<T>, file: File) = kotlin.runCatching {
        decodeFromString(deserializer, file.readText())
    }

    inline fun <reified T> StringFormat.parse(file: File): Result<T> {
        return parse(serializer(), file)
    }

    /**
     * Attempt to parse. Returns default [factory] value on failure
     */
    fun <T> StringFormat.parseOrDefault(deserializer: DeserializationStrategy<T>, file: File, factory: () -> T): T {
        return parse<T>(deserializer, file).getOrElse { factory.invoke() }
    }

    inline fun <reified T> StringFormat.parseOrDefault(file: File, noinline factory: () -> T): T {
        return parseOrDefault(serializer(), file, factory)
    }

    /**
     * Converts [value] into string by [encodeToString] and write text into file [file]
     */
    fun <T> StringFormat.writeIntoFile(serializer: SerializationStrategy<T>, value: T, file: File) {
        val string = encodeToString(serializer, value)
        file.writeText(string)
    }

    inline fun <reified T> StringFormat.writeIntoFile(value: T, file: File) {
        writeIntoFile(serializer(), value, file)
    }
}

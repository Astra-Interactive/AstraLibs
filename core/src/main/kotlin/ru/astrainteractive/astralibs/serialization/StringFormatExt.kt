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
        if (!file.exists()) error("Could not find file ${file.absolutePath}")
        decodeFromString(deserializer, file.readText())
    }

    inline fun <reified T> StringFormat.parse(file: File): Result<T> {
        return parse(serializer(), file)
    }

    /**
     * Parse value or return null if not parsed
     */
    fun <T : Any> StringFormat.parseOrNull(deserializer: DeserializationStrategy<T>, file: File): T? {
        return parse<T>(deserializer, file).getOrNull()
    }

    inline fun <reified T : Any> StringFormat.parseOrNull(file: File): T? {
        return parseOrNull(serializer(), file)
    }

    /**
     * Attempt to parse. Returns default [factory] value on failure
     */
    fun <T> StringFormat.parseOrDefault(deserializer: DeserializationStrategy<T>, file: File, factory: () -> T): T {
        if (!file.exists() || file.length() == 0L) return factory.invoke()
        return parse(deserializer, file).getOrElse { factory.invoke() }
    }

    inline fun <reified T> StringFormat.parseOrDefault(file: File, noinline factory: () -> T): T {
        return parseOrDefault(serializer(), file, factory)
    }

    /**
     * Converts [value] into string by [encodeToString] and write text into file [file]
     */
    fun <T> StringFormat.writeIntoFile(serializer: SerializationStrategy<T>, value: T, file: File) {
        val string = encodeToString(serializer, value)
        if (file.parentFile?.exists() != true) file.parentFile?.mkdirs()
        if (!file.exists()) file.createNewFile()
        file.writeText(string)
    }

    inline fun <reified T> StringFormat.writeIntoFile(value: T, file: File) {
        writeIntoFile(serializer(), value, file)
    }
}

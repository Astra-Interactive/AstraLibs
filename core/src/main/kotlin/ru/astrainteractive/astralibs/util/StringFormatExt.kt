package ru.astrainteractive.astralibs.util

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import ru.astrainteractive.klibs.mikro.core.logging.StubLogger
import java.io.File

/** Deserializes [file] using [deserializer]; returns a failure if the file is missing or unparseable. */
fun <T> StringFormat.parse(deserializer: DeserializationStrategy<T>, file: File) = runCatching {
    if (!file.exists()) error("Could not find file ${file.absolutePath}")
    decodeFromString(deserializer, file.readText())
}

/** Deserializes [file] using the default serializer for [T]. */
inline fun <reified T> StringFormat.parse(file: File): Result<T> {
    return parse(serializer(), file)
}

/** Deserializes [file]; returns `null` on any failure. */
fun <T : Any> StringFormat.parseOrNull(deserializer: DeserializationStrategy<T>, file: File): T? {
    return parse<T>(deserializer, file).getOrNull()
}

/** Deserializes [file] using the default serializer; returns `null` on any failure. */
inline fun <reified T : Any> StringFormat.parseOrNull(file: File): T? {
    return parseOrNull(serializer(), file)
}

/** Deserializes [file]; returns [factory]'s value when the file is missing or unparseable. */
fun <T> StringFormat.parseOrDefault(deserializer: DeserializationStrategy<T>, file: File, factory: () -> T): T {
    if (!file.exists() || file.length() == 0L) return factory.invoke()
    return parse(deserializer, file).getOrElse { factory.invoke() }
}

/** Deserializes [file] using the default serializer; returns [factory]'s value on failure. */
inline fun <reified T> StringFormat.parseOrDefault(file: File, noinline factory: () -> T): T {
    return parseOrDefault(serializer(), file, factory)
}

/** Serializes [value] and writes it to [file], creating parent directories and the file if absent. */
fun <T> StringFormat.writeIntoFile(serializer: SerializationStrategy<T>, value: T, file: File) {
    val string = encodeToString(serializer, value)
    if (file.parentFile?.exists() != true) file.parentFile?.mkdirs()
    if (!file.exists()) file.createNewFile()
    file.writeText(string)
}

/** Serializes [value] using the default serializer and writes it to [file]. */
inline fun <reified T> StringFormat.writeIntoFile(value: T, file: File) {
    writeIntoFile(serializer(), value, file)
}

/**
 * Tries to parse [file]; on failure logs the error, writes [default] to a fallback file, and returns [default].
 * On success, writes the parsed value back to [file] and returns it.
 *
 * The fallback file is [file] itself when missing/empty, or `<name>.default.<ext>` otherwise.
 */
fun <T> StringFormat.parseOrWriteIntoDefault(
    serializer: KSerializer<T>,
    file: File,
    logger: Logger = StubLogger,
    default: () -> T
): T {
    val folder = file.parentFile
    if (!folder.exists()) folder.mkdirs()
    return parse(serializer, file)
        .onFailure { throwable ->
            logger.error {
                "#parseOrWriteIntoDefault could not parse file"
                    .plus("${file.name}: ")
                    .plus("${(throwable.message ?: throwable.localizedMessage)}")
            }
            val defaultFile = when {
                !file.exists() || file.length() == 0L -> file
                else -> folder.resolve("${file.nameWithoutExtension}.default.${file.extension}")
            }
            if (!defaultFile.exists()) {
                defaultFile.createNewFile()
            }
            writeIntoFile(serializer, default.invoke(), defaultFile)
        }
        .onSuccess { writeIntoFile(serializer, it, file) }
        .getOrElse { default.invoke() }
}

/** Reified overload of [parseOrWriteIntoDefault]. */
inline fun <reified T> StringFormat.parseOrWriteIntoDefault(
    file: File,
    logger: Logger = StubLogger,
    noinline default: () -> T
): T = parseOrWriteIntoDefault(
    serializer = serializer(),
    file = file,
    logger = logger,
    default = default
)

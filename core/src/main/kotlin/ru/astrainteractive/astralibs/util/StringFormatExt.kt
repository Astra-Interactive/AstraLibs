package ru.astrainteractive.astralibs.util

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import java.io.File


/**
 * Parses an object from the specified file using the provided deserializer.
 * If the file does not exist, an error is thrown.
 *
 * @param deserializer The deserialization strategy for the target type.
 * @param file The file containing the serialized data.
 * @return A [Result] containing the deserialized object, or an exception if parsing fails.
 * @param T The type of the object to parse.
 */
fun <T> StringFormat.parse(deserializer: DeserializationStrategy<T>, file: File) = runCatching {
    if (!file.exists()) error("Could not find file ${file.absolutePath}")
    decodeFromString(deserializer, file.readText())
}

/**
 * Parses an object of type [T] from the specified file using the default serializer.
 *
 * @param file The file containing the serialized data.
 * @return A [Result] containing the deserialized object, or an exception if parsing fails.
 * @param T The type of the object to parse.
 */
inline fun <reified T> StringFormat.parse(file: File): Result<T> {
    return parse(serializer(), file)
}

/**
 * Parses an object from the specified file using the provided deserializer, returning null if parsing fails.
 *
 * @param deserializer The deserialization strategy for the target type.
 * @param file The file containing the serialized data.
 * @return The deserialized object, or null if parsing fails.
 * @param T The type of the object to parse.
 */
fun <T : Any> StringFormat.parseOrNull(deserializer: DeserializationStrategy<T>, file: File): T? {
    return parse<T>(deserializer, file).getOrNull()
}

/**
 * Parses an object of type [T] from the specified file using the default serializer,
 * returning null if parsing fails.
 *
 * @param file The file containing the serialized data.
 * @return The deserialized object, or null if parsing fails.
 * @param T The type of the object to parse.
 */
inline fun <reified T : Any> StringFormat.parseOrNull(file: File): T? {
    return parseOrNull(serializer(), file)
}

/**
 * Parses an object from the specified file using the provided deserializer.
 * If the file does not exist or parsing fails, returns a default value provided by the [factory].
 *
 * @param deserializer The deserialization strategy for the target type.
 * @param file The file containing the serialized data.
 * @param factory A function that creates a default value if parsing fails.
 * @return The parsed object, or the default value if the file is missing or parsing fails.
 * @param T The type of the object to parse.
 */
fun <T> StringFormat.parseOrDefault(deserializer: DeserializationStrategy<T>, file: File, factory: () -> T): T {
    if (!file.exists() || file.length() == 0L) return factory.invoke()
    return parse(deserializer, file).getOrElse { factory.invoke() }
}

/**
 * Parses an object of type [T] from the specified file using the default serializer.
 * If the file does not exist or parsing fails, returns a default value provided by the [factory].
 *
 * @param file The file containing the serialized data.
 * @param factory A function that creates a default value if parsing fails.
 * @return The parsed object, or the default value if the file is missing or parsing fails.
 * @param T The type of the object to parse.
 */
inline fun <reified T> StringFormat.parseOrDefault(file: File, noinline factory: () -> T): T {
    return parseOrDefault(serializer(), file, factory)
}

/**
 * Serializes the specified value and writes it into the specified file.
 * If necessary, creates missing parent directories and the file itself.
 *
 * @param serializer The serialization strategy for the target type.
 * @param value The object to serialize.
 * @param file The target file to write the serialized data into.
 * @param T The type of the object to serialize.
 */
fun <T> StringFormat.writeIntoFile(serializer: SerializationStrategy<T>, value: T, file: File) {
    val string = encodeToString(serializer, value)
    if (file.parentFile?.exists() != true) file.parentFile?.mkdirs()
    if (!file.exists()) file.createNewFile()
    file.writeText(string)
}

/**
 * Serializes the specified value of type [T] and writes it into the specified file using the default serializer.
 * If necessary, creates missing parent directories and the file itself.
 *
 * @param value The object to serialize.
 * @param file The target file to write the serialized data into.
 * @param T The type of the object to serialize.
 */
inline fun <reified T> StringFormat.writeIntoFile(value: T, file: File) {
    writeIntoFile(serializer(), value, file)
}

/**
 * Attempts to parse a file or writes a default value to a default file if parsing fails.
 *
 * This function tries to parse the content of a specified file using the given serializer.
 * If parsing fails, it logs an error, creates a default file if necessary, writes the default value
 * to this file, and returns the default value. If parsing is successful, it writes the parsed value
 * back to the original file and returns the parsed value.
 *
 * @param serializer The serializer used to parse the file content.
 * @param file The file to be parsed.
 * @param logger The logger used to log error messages. Defaults to a [StubLogger].
 * @param default A lambda function that provides the default value of type [T] if parsing fails.
 * @return The parsed value of type [T] if successful, otherwise the default value.
 *
 * @property folder The parent directory of the file. Created if it does not exist.
 * @property defaultFile The file to which the default value is written if parsing fails.
 * This is the original file if it does not exist or is empty, otherwise a new file with a ".default" suffix.
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

/**
 * Attempts to parse a file or writes a default value to a default file if parsing fails.
 *
 * This function tries to parse the content of a specified file using the given serializer.
 * If parsing fails, it logs an error, creates a default file if necessary, writes the default value
 * to this file, and returns the default value. If parsing is successful, it writes the parsed value
 * back to the original file and returns the parsed value.
 *
 * @param file The file to be parsed.
 * @param logger The logger used to log error messages. Defaults to a [StubLogger].
 * @param default A lambda function that provides the default value of type [T] if parsing fails.
 * @return The parsed value of type [T] if successful, otherwise the default value.
 */
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

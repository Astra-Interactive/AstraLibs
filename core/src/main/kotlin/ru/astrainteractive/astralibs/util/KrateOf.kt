package ru.astrainteractive.astralibs.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import ru.astrainteractive.klibs.mikro.core.logging.StubLogger
import java.io.File

/**
 * @return failure for a file that cannot be parsed, which is left as it is with a `<name>.default.<ext>` template
 * written next to it; a missing or empty file gets [factory]'s value written into it
 */
private fun <T> StringFormat.readOrWriteDefault(
    serializer: KSerializer<T>,
    file: File,
    factory: ValueFactory<T>,
    logger: Logger
): Result<T> {
    if (!file.exists() || file.length() == 0L) {
        val default = factory.create()
        writeIntoFile(serializer, default, file)
        return Result.success(default)
    }
    return parse(serializer, file)
        .onSuccess { value -> writeIntoFile(serializer, value, file) }
        .onFailure { error ->
            logger.error { "#krateOf could not parse ${file.name}, keeping the last value: ${error.message}" }
            val template = file.resolveSibling("${file.nameWithoutExtension}.default.${file.extension}")
            writeIntoFile(serializer, factory.create(), template)
        }
}

/**
 * Creates a krate backed by [file] whose cached value is the value last read or saved.
 *
 * Loading parses [file] and writes it back; a missing or empty file gets [factory]'s value. A file that cannot be
 * parsed is left untouched and the krate keeps its value, which is [factory]'s value only when nothing was read
 * yet, so one typo does not reset a running plugin. Saving `null` deletes [file].
 */
fun <T> StringFormat.krateOf(
    serializer: KSerializer<T>,
    file: File,
    factory: ValueFactory<T>,
    logger: Logger = StubLogger
): StateFlowMutableKrate<T> = FileKrate(
    read = { readOrWriteDefault(serializer, file, factory, logger) },
    write = { value ->
        if (value == null) {
            file.delete()
        } else {
            writeIntoFile(serializer, value, file)
        }
    },
    factory = factory,
    lockOwner = LockOwner.Default()
)

/** @see krateOf */
inline fun <reified T> StringFormat.krateOf(
    file: File,
    factory: ValueFactory<T>,
    logger: Logger = StubLogger
): StateFlowMutableKrate<T> = krateOf(
    serializer = serializer(),
    file = file,
    factory = factory,
    logger = logger
)

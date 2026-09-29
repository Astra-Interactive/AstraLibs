package ru.astrainteractive.astralibs.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import ru.astrainteractive.klibs.mikro.core.logging.StubLogger
import java.io.File

/**
 * Reads [file] for [krateOf]: a missing or empty file gets the default written into it, and a file that stops
 * parsing is left as it is and gives [lastValue] instead of the default, so one typo does not reset the value.
 */
@PublishedApi
internal fun <T> StringFormat.parseOrLastValue(
    serializer: KSerializer<T>,
    file: File,
    lastValue: KrateLastValue<T>,
    logger: Logger,
    default: () -> T
): T {
    val folder = file.parentFile
    if (!folder.exists()) folder.mkdirs()
    val parsed = parse(serializer, file)
    val parsedValue = parsed.getOrElse { error ->
        if (!file.exists() || file.length() == 0L) {
            val defaultValue = default.invoke()
            writeIntoFile(serializer, defaultValue, file)
            lastValue.remember(defaultValue)
            return defaultValue
        }
        logger.error { "#parseOrLastValue could not parse ${file.name}, keeping the last value: ${error.message}" }
        val defaultFile = folder.resolve("${file.nameWithoutExtension}.default.${file.extension}")
        writeIntoFile(serializer, default.invoke(), defaultFile)
        return lastValue.orElse(default)
    }
    writeIntoFile(serializer, parsedValue, file)
    lastValue.remember(parsedValue)
    return parsedValue
}

/**
 * Creates a [DefaultMutableKrate] backed by [file].
 *
 * Loading parses [file] and writes it back; a missing or empty file gets [factory]'s value. A file that cannot
 * be parsed is left untouched and loading keeps the value last read or saved, or [factory]'s value when there is
 * none yet. Saving writes the value back to [file], or deletes [file] when the value is `null`.
 */
inline fun <reified T> StringFormat.krateOf(
    file: File,
    factory: ValueFactory<T>,
    logger: Logger = StubLogger
): DefaultMutableKrate<T> {
    val lastValue = KrateLastValue<T?>()
    return DefaultMutableKrate(
        factory = factory,
        loader = {
            parseOrLastValue<T?>(
                serializer = serializer(),
                file = file,
                lastValue = lastValue,
                logger = logger,
                default = factory::create
            )
        },
        saver = { value ->
            if (value == null) {
                file.delete()
                lastValue.forget()
            } else {
                writeIntoFile<T>(
                    value = value,
                    file = file
                )
                lastValue.remember(value)
            }
        }
    )
}

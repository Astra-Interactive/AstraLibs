package ru.astrainteractive.astralibs.util

import kotlinx.serialization.StringFormat
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import java.io.File

/**
 * Creates a [DefaultMutableKrate] backed by [file].
 *
 * Loading parses [file] via [parseOrWriteIntoDefault], falling back to [factory]'s value when the file is
 * missing or unparseable. Saving writes the value back to [file], or deletes [file] when the value is `null`.
 */
inline fun <reified T> StringFormat.krateOf(
    file: File,
    factory: ValueFactory<T>
): DefaultMutableKrate<T> = DefaultMutableKrate(
    factory = factory,
    loader = {
        parseOrWriteIntoDefault<T?>(
            file = file,
            default = factory::create
        )
    },
    saver = { value ->
        if (value == null) {
            file.delete()
        } else {
            writeIntoFile<T>(
                value = value,
                file = file
            )
        }
    }
)

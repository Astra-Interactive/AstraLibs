package ru.astrainteractive.astralibs.krate.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import ru.astrainteractive.astralibs.serialization.StringFormatExt.parse
import ru.astrainteractive.astralibs.serialization.StringFormatExt.writeIntoFile
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import java.io.File

class StringFormatKrate<T>(
    stringFormat: StringFormat,
    kSerializer: KSerializer<T>,
    factory: ValueFactory<T>,
    fileName: String,
    folder: File,
) : FileKrate<T> by FileKrate.Default(
    factory = factory,
    fileName = fileName,
    folder = folder,
    save = save@{ file, value ->
        stringFormat.writeIntoFile(kSerializer, value, file)
    },
    load = load@{ file ->
        stringFormat.parse(kSerializer, file).getOrThrow()
    }
)

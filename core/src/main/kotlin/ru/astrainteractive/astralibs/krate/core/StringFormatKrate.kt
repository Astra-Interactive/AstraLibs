package ru.astrainteractive.astralibs.krate.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import ru.astrainteractive.astralibs.serialization.StringFormatExt.parse
import ru.astrainteractive.astralibs.serialization.StringFormatExt.writeIntoFile
import java.io.File

class StringFormatKrate<T>(
    stringFormat: StringFormat,
    kSerializer: KSerializer<T>,
    default: T,
    fileName: String,
    folder: File,
) : FileKrate<T> by FileKrate.Default(
    default = default,
    fileName = fileName,
    folder = folder,
    save = save@{ file, value ->
        stringFormat.writeIntoFile(kSerializer, value, file)
    },
    load = load@{ file ->
        stringFormat.parse(kSerializer, file).getOrThrow()
    }
)

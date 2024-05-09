package ru.astrainteractive.astralibs.krate.core

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import java.io.File

class BinaryFormatKrate<T>(
    binaryFormat: BinaryFormat,
    kSerializer: KSerializer<T>,
    default: T,
    key: String,
    folder: File,
) : FileKrate<T> by FileKrate.Default(
    default = default,
    fileName = key,
    folder = folder,
    save = save@{ file, value ->
        val byteArray = binaryFormat.encodeToByteArray(kSerializer, value)
        file.writeBytes(byteArray)
    },
    load = load@{ file ->
        binaryFormat.decodeFromByteArray(kSerializer, file.readBytes())
    }
)

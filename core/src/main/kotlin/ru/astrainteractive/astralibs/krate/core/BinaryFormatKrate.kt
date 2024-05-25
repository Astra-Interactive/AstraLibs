package ru.astrainteractive.astralibs.krate.core

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import java.io.File

class BinaryFormatKrate<T>(
    binaryFormat: BinaryFormat,
    kSerializer: KSerializer<T>,
    factory: ValueFactory<T>,
    key: String,
    folder: File,
) : FileKrate<T> by FileKrate.Default(
    factory = factory,
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

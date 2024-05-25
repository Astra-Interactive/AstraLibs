package ru.astrainteractive.astralibs.krate.core

import ru.astrainteractive.astralibs.encoding.encoder.ObjectEncoder
import ru.astrainteractive.astralibs.encoding.model.EncodedObject
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import java.io.File
import java.io.Serializable

class JBinaryKrate<T : Serializable>(
    factory: ValueFactory<T>,
    objectEncoder: ObjectEncoder,
    fileName: String,
    folder: File,
) : FileKrate<T> by FileKrate.Default(
    factory = factory,
    folder = folder,
    fileName = fileName,
    save = save@{ file, value ->
        val bytes = objectEncoder.toByteArray(value).value
        file.writeBytes(bytes)
    },
    load = load@{ file ->
        objectEncoder.fromByteArray(EncodedObject.ByteArray(file.readBytes()))
    }
)

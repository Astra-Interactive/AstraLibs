package ru.astrainteractive.astralibs.filestorage

import ru.astrainteractive.astralibs.serialization.Serializer

interface FileStorageValueProvider {
    val serializer: Serializer
    val extension: String

    class Default(
        override val serializer: Serializer,
        override val extension: String
    ) : FileStorageValueProvider
}

package ru.astrainteractive.astralibs.filestorage

import ru.astrainteractive.astralibs.serialization.YamlSerializer

object YamlFileStorageValueProvider : FileStorageValueProvider by FileStorageValueProvider.Default(
    serializer = YamlSerializer(),
    extension = "yaml"
)

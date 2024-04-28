package ru.astrainteractive.astralibs.krate

import ru.astrainteractive.astralibs.serialization.YamlStringFormat
import java.io.File

class YamlKrateFactory(private val folder: File) : KrateFactory by KrateFactory.Default(
    stringFormat = SERIALIZER,
    extension = EXTENSION,
    folder = folder,
) {
    companion object {
        private val SERIALIZER = YamlStringFormat()
        private const val EXTENSION = "yaml"
    }
}

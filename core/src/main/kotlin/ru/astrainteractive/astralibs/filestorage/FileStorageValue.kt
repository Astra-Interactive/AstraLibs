package ru.astrainteractive.astralibs.filestorage

import kotlinx.serialization.KSerializer
import ru.astrainteractive.astralibs.serialization.Serializer
import ru.astrainteractive.astralibs.serialization.SerializerExt.parse
import ru.astrainteractive.astralibs.serialization.SerializerExt.writeIntoFile
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import java.io.File

class FileStorageValue<T>(
    serializer: Serializer,
    kSerializer: KSerializer<T>,
    default: () -> T,
    val key: String,
    val folder: File,
) : MutableStorageValue<T> by MutableStorageValue(
    default = default.invoke(),
    saveSettingsValue = saveSettingsValue@{ value ->
        val file = File(folder, key)
        file.parentFile.mkdirs()
        file.createNewFile()
        serializer.writeIntoFile(kSerializer, value, file)
    },
    loadSettingsValue = loadSettingsValue@{
        val file = File(folder, key)
        if (!file.exists()) return@loadSettingsValue default.invoke()
        serializer.parse(kSerializer, file).getOrNull() ?: default.invoke()
    }
)

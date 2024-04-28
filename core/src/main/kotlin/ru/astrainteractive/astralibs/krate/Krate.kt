package ru.astrainteractive.astralibs.krate

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import ru.astrainteractive.astralibs.serialization.StringFormatExt.parse
import ru.astrainteractive.astralibs.serialization.StringFormatExt.writeIntoFile
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import java.io.File

/**
 * [Krate] is a holder for  a file.
 *
 * With this you can easily create serialized file holders
 */
class Krate<T>(
    stringFormat: StringFormat,
    kSerializer: KSerializer<T>,
    default: T,
    val key: String,
    val folder: File,
) : MutableStorageValue<T> by MutableStorageValue(
    default = default,
    saveSettingsValue = saveSettingsValue@{ value ->
        val file = File(folder, key)
        file.parentFile.mkdirs()
        file.createNewFile()
        stringFormat.writeIntoFile(kSerializer, value, file)
    },
    loadSettingsValue = loadSettingsValue@{
        val file = File(folder, key)
        if (!file.exists()) return@loadSettingsValue default
        stringFormat.parse(kSerializer, file).getOrNull() ?: default
    }
)

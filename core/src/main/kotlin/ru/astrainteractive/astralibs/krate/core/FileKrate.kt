package ru.astrainteractive.astralibs.krate.core

import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import java.io.File

/**
 * [FileKrate] is a holder for a file.
 *
 * With this you can easily create serialized file holders
 */
interface FileKrate<T> : MutableStorageValue<T> {
    val fileName: String
    val folder: File

    class Default<T>(
        val default: T,
        override val fileName: String,
        override val folder: File,
        save: (file: File, value: T) -> Unit,
        load: (file: File) -> T
    ) : FileKrate<T>,
        MutableStorageValue<T> by MutableStorageValue(
            default = default,
            saveSettingsValue = saveSettingsValue@{ value ->
                val file = File(folder, fileName)
                file.parentFile.mkdirs()
                file.createNewFile()
                save.invoke(file, value)
            },
            loadSettingsValue = loadSettingsValue@{
                val file = File(folder, fileName)
                if (!file.exists()) return@loadSettingsValue default
                runCatching { load.invoke(file) }.getOrElse { default }
            }
        )
}

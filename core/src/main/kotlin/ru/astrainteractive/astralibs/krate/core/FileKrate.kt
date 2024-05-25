package ru.astrainteractive.astralibs.krate.core

import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
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
        val factory: ValueFactory<T>,
        override val fileName: String,
        override val folder: File,
        save: (file: File, value: T) -> Unit,
        load: (file: File) -> T
    ) : FileKrate<T>,
        MutableStorageValue<T> by MutableStorageValue(
            factory = factory,
            saver = saver@{ value ->
                val file = File(folder, fileName)
                file.parentFile.mkdirs()
                file.createNewFile()
                save.invoke(file, value)
            },
            loader = loader@{
                val file = File(folder, fileName)
                if (!file.exists()) return@loader factory.create()
                runCatching { load.invoke(file) }.getOrElse { factory.create() }
            }
        )
}

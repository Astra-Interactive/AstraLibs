package ru.astrainteractive.astralibs.filestorage

import kotlinx.serialization.serializer
import java.io.File

object FileStorageExt {
    /**
     * Delete entire storage file
     */
    fun FileStorageValue<*>.delete() {
        File(folder, key).delete()
    }

    inline fun <reified T> FileStorageValueProvider.provide(
        noinline default: () -> T,
        key: String,
        folder: File = File("./temp")
    ) = FileStorageValue(
        serializer = serializer,
        kSerializer = serializer.serializersModule.serializer<T>(),
        default = default,
        folder = folder,
        key = "$key.$extension"
    )
}

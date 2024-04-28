package ru.astrainteractive.astralibs.krate

import kotlinx.serialization.serializer
import java.io.File

object KrateExt {
    /**
     * Delete entire storage file
     */
    fun Krate<*>.delete() {
        File(folder, key).delete()
    }

    /**
     * Delete entire storage
     */
    fun Krate<*>.deleteAll() {
        folder.delete()
    }

    inline fun <reified T> KrateFactory.create(
        key: String,
        noinline default: () -> T
    ): Krate<T> = create(
        kserializer = serializer(),
        key = key,
        default = default
    )
}

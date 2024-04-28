package ru.astrainteractive.astralibs.krate

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.io.File

/**
 * Create custom factories for [Krate] to reduce code boilerplate
 */
interface KrateFactory {
    fun <T> create(
        kserializer: KSerializer<T>,
        key: String,
        default: () -> T
    ): Krate<T>

    class Default(
        private val stringFormat: StringFormat,
        private val extension: String,
        private val folder: File,
    ) : KrateFactory {
        override fun <T> create(
            kserializer: KSerializer<T>,
            key: String,
            default: () -> T
        ): Krate<T> = Krate(
            stringFormat = stringFormat,
            kSerializer = kserializer,
            default = default.invoke(),
            folder = folder,
            key = "$key.$extension"
        )
    }
}

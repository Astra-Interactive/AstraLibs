package ru.astrainteractive.astralibs.configloader

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

/**
 * This [ConfigLoader] will help you to parse data from your config.yml file
 */
object ConfigLoader {
    val defaultYamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    )
    val yamlDecoder = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = defaultYamlConfiguration
    )

    /**
     * Parses [file] into [T] and throws exception if can't
     */
    inline fun <reified T> unsafeParse(file: File): T {
        return yamlDecoder.decodeFromString(file.readText())
    }

    /**
     * Return kotlin's [Result]
     */
    inline fun <reified T> safeParse(file: File) = kotlin.runCatching {
        unsafeParse<T>(file)
    }

    inline fun <reified T : Any> toClassOrDefault(file: File, default: T): T {
        return kotlin.runCatching {
            unsafeParse<T>(file)
        }.onFailure {
            it.printStackTrace()
            val yamlText = yamlDecoder.encodeToString(default)
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText(yamlText)
        }.getOrNull() ?: default
    }
}
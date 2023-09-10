package ru.astrainteractive.astralibs.configloader

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.astrainteractive.klibs.kdi.Factory
import java.io.File

/**
 * This [ConfigLoader] will help you to parse data from your config.yml file
 */
class ConfigLoader(
    val configuration: YamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    ),
    val yaml: Yaml = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = configuration
    )
) {

    /**
     * Parses [file] into [T] and throws exception if can't
     */
    inline fun <reified T> unsafeParse(file: File): T {
        return yaml.decodeFromString(file.readText())
    }

    /**
     * Return kotlin's [Result]
     */
    inline fun <reified T> safeParse(file: File) = kotlin.runCatching {
        unsafeParse<T>(file)
    }

    /**
     * Trying to parse [file] via [safeParse] and loadng [default] if failure happened
     */
    inline fun <reified T : Any> toClassOrDefault(file: File, default: Factory<T>): T {
        return kotlin.runCatching {
            unsafeParse<T>(file)
        }.onFailure {
            it.printStackTrace()
            val yamlText = yaml.encodeToString(default.create())
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText(yamlText)
        }.getOrNull() ?: default.create()
    }
}

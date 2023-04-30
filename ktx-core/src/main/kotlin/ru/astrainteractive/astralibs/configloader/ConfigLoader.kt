package ru.astrainteractive.astralibs.configloader

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.astrainteractive.astralibs.Factory
import java.io.File

/**
 * This [ConfigLoader] will help you to parse data from your config.yml file
 */
object ConfigLoader {
    val defaultYamlConfiguration: YamlConfiguration
        get() = Yaml.default.configuration.copy(
            encodeDefaults = true,
            strictMode = false
        )
    val defaultYaml: Yaml
        get() = Yaml(
            serializersModule = Yaml.default.serializersModule,
            configuration = defaultYamlConfiguration
        )

    /**
     * Parses [file] into [T] and throws exception if can't
     */
    inline fun <reified T> unsafeParse(file: File, yaml: Yaml = defaultYaml): T {
        return yaml.decodeFromString(file.readText())
    }

    /**
     * Return kotlin's [Result]
     */
    inline fun <reified T> safeParse(file: File, yaml: Yaml = defaultYaml) = kotlin.runCatching {
        unsafeParse<T>(file, yaml)
    }

    /**
     * Trying to parse [file] via [safeParse] and loadng [default] if failure happened
     */
    inline fun <reified T : Any> toClassOrDefault(file: File, default: Factory<T>, yaml: Yaml = defaultYaml): T {
        return kotlin.runCatching {
            unsafeParse<T>(file, yaml)
        }.onFailure {
            it.printStackTrace()
            val yamlText = yaml.encodeToString(default.build())
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText(yamlText)
        }.getOrNull() ?: default.build()
    }
}

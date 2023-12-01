package ru.astrainteractive.astralibs.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

/**
 * This [YamlSerializer] will help you to parse data from your config.yml file
 */
class YamlSerializer(
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
     * Attempts to parse content of [file] and return [T] as kotlin's result
     */
    inline fun <reified T> parse(file: File) = kotlin.runCatching {
        yaml.decodeFromString<T>(file.readText())
    }

    /**
     * Attempt to parse. Returns default [factory] value on failure
     */
    inline fun <reified T> parseOrDefault(file: File, factory: () -> T): T {
        return parse<T>(file).getOrElse { factory.invoke() }
    }

    /**
     * Convert [value] into pure yaml string
     */
    inline fun <reified T> encodeToString(value: T): String {
        return yaml.encodeToString(value)
    }

    /**
     * Converts [value] into string by [encodeToString] and write text into file [file]
     */
    inline fun <reified T> writeIntoFile(value: T, file: File) {
        val string = encodeToString(value)
        file.writeText(string)
    }
}

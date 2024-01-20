package ru.astrainteractive.astralibs.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule

/**
 * This [YamlSerializer] will help you to parse data from your config.yml file
 */
@Suppress("MemberVisibilityCanBePrivate")
class YamlSerializer(
    val configuration: YamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    ),
    val yaml: Yaml = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = configuration
    )
) : Serializer {
    override val serializersModule: SerializersModule
        get() = yaml.serializersModule

    override fun <T> decodeFromString(serializer: DeserializationStrategy<T>, string: String): T {
        return yaml.decodeFromString(serializer, string)
    }

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        return yaml.encodeToString(serializer, value)
    }
}

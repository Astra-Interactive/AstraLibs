package ru.astrainteractive.astralibs.util

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.StringFormat

/**
 * A YAML-based implementation of [kotlinx.serialization.StringFormat] that uses [com.charleskorn.kaml.Yaml] for
 * serialization and deserialization of YAML data.
 */
@Suppress("MemberVisibilityCanBePrivate")
class YamlStringFormat(
    val configuration: YamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    ),
    val yaml: Yaml = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = configuration
    )
) : StringFormat by yaml
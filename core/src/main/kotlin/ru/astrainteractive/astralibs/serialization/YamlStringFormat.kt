package ru.astrainteractive.astralibs.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.StringFormat

/**
 * A YAML-based implementation of [StringFormat] that uses [Yaml] for
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

package ru.astrainteractive.astralibs.util

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.StringFormat

public typealias YamlStringFormat = StringFormat

/** [StringFormat] of [Yaml] with `encodeDefaults = true` and `strictMode = false`. */
public fun YamlStringFormat(
    configuration: YamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    ),
    yaml: Yaml = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = configuration
    )
): YamlStringFormat = yaml

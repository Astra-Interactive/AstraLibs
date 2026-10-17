package ru.astrainteractive.astralibs.util

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.StringFormat

/** [StringFormat] wrapping [Yaml] with `encodeDefaults = true` and `strictMode = false`. */
@Suppress("MemberVisibilityCanBePrivate")
public class YamlStringFormat(
    public val configuration: YamlConfiguration = Yaml.default.configuration.copy(
        encodeDefaults = true,
        strictMode = false
    ),
    public val yaml: Yaml = Yaml(
        serializersModule = Yaml.default.serializersModule,
        configuration = configuration
    )
) : StringFormat by yaml

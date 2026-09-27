package ru.astrainteractive.astralibs.localization.locale

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Locale

/**
 * Stores a [Locale] as a Minecraft language code (`ru_ru`), for configuration fields that name a language.
 *
 * A blank code fails the whole file instead of silently becoming an unknown language, so a typo shows up in the
 * load log.
 */
object MinecraftLocaleSerializer : KSerializer<Locale> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "ru.astrainteractive.astralibs.localization.locale.MinecraftLocale",
        kind = PrimitiveKind.STRING
    )

    /**
     * Parses a language code the way the Minecraft client sends it: `ru_ru`, `en_us`, or `enws` without a country.
     * [Locale.forLanguageTag] is not used because it rejects codes such as `enws`.
     *
     * @return null when [code] is blank
     */
    fun parse(code: String): Locale? {
        val trimmedCode = code.trim()
        if (trimmedCode.isEmpty()) return null
        val language = trimmedCode.substringBefore('_')
        val country = trimmedCode.substringAfter('_', missingDelimiterValue = "")
        return Locale.of(language, country)
    }

    /** Formats [locale] as a Minecraft language code, e.g. `ru_ru`; [Locale.ROOT] becomes an empty string. */
    internal fun format(locale: Locale): String {
        return listOf(locale.language, locale.country)
            .filter { part -> part.isNotEmpty() }
            .joinToString(separator = "_")
            .lowercase(Locale.ROOT)
    }

    override fun serialize(encoder: Encoder, value: Locale) {
        encoder.encodeString(format(value))
    }

    override fun deserialize(decoder: Decoder): Locale {
        val code = decoder.decodeString()
        return parse(code) ?: throw SerializationException("Language code is blank")
    }
}

@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.localization.locale

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerializationException
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MinecraftLocaleTest {
    @Test
    fun GIVEN_client_code_WHEN_parse_THEN_language_and_country_are_split() {
        assertEquals(MinecraftLocales.RU_RU, MinecraftLocaleSerializer.parse("ru_ru"))
    }

    @Test
    fun GIVEN_upper_case_code_with_spaces_WHEN_parse_THEN_locale_is_normalized() {
        assertEquals(MinecraftLocales.EN_US, MinecraftLocaleSerializer.parse(" EN_US "))
    }

    @Test
    fun GIVEN_code_without_country_WHEN_parse_THEN_locale_has_language_only() {
        val locale = MinecraftLocaleSerializer.parse("enws")

        assertEquals("enws", locale?.language)
        assertEquals("", locale?.country)
    }

    @Test
    fun GIVEN_blank_code_WHEN_parse_THEN_returns_null() {
        assertNull(MinecraftLocaleSerializer.parse("  "))
    }

    @Test
    fun GIVEN_locales_WHEN_format_THEN_client_codes_are_lower_case() {
        assertEquals("en_us", MinecraftLocaleSerializer.format(MinecraftLocales.EN_US))
        assertEquals("enws", MinecraftLocaleSerializer.format(Locale.of("enws")))
        assertEquals("", MinecraftLocaleSerializer.format(Locale.ROOT))
    }

    @Test
    fun GIVEN_locale_field_WHEN_encode_and_decode_THEN_code_round_trips() {
        val encoded = Yaml.default.encodeToString(MinecraftLocaleSerializer, MinecraftLocales.RU_RU)

        assertEquals("\"ru_ru\"", encoded)
        assertEquals(MinecraftLocales.RU_RU, Yaml.default.decodeFromString(MinecraftLocaleSerializer, encoded))
    }

    @Test
    fun GIVEN_blank_locale_field_WHEN_decode_THEN_loading_fails_instead_of_guessing() {
        assertFailsWith<SerializationException> {
            Yaml.default.decodeFromString(MinecraftLocaleSerializer, "\" \"")
        }
    }
}

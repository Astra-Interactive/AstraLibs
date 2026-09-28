@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.localization.text

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocales
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LocalizedTextSerializerTest {
    private val yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = true, strictMode = false))

    private val emptyText = LocalizedText(sharedText = null, translationByLocale = emptyMap())

    private fun decode(source: String): LocalizedText {
        return yaml.decodeFromString(LocalizedTextSerializer, source)
    }

    private fun encode(value: LocalizedText): String {
        return yaml.encodeToString(LocalizedTextSerializer, value)
    }

    @Test
    fun GIVEN_scalar_WHEN_decode_THEN_text_is_shared_by_every_language() {
        assertEquals(LocalizedText.shared("&aHi"), decode("\"&aHi\""))
    }

    @Test
    fun GIVEN_map_of_language_codes_WHEN_decode_THEN_translations_keep_their_order() {
        val decoded = decode("ru_ru: Привет\nen_us: Hello")

        val expected = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "Привет")
            translation(MinecraftLocales.EN_US, "Hello")
        }
        assertEquals(expected, decoded)
        assertEquals(listOf(MinecraftLocales.RU_RU, MinecraftLocales.EN_US), decoded.translationByLocale.keys.toList())
    }

    @Test
    fun GIVEN_star_key_WHEN_decode_THEN_it_holds_shared_text() {
        val source = """
            en_us: Hello
            "*": Hi
        """.trimIndent()

        val decoded = decode(source)

        val expected = LocalizedText.build {
            translation(MinecraftLocales.EN_US, "Hello")
            shared("Hi")
        }
        assertEquals(expected, decoded)
    }

    @Test
    fun GIVEN_blank_key_WHEN_decode_THEN_it_holds_shared_text() {
        assertEquals(LocalizedText.shared("Hi"), decode("'': Hi"))
    }

    @Test
    fun GIVEN_upper_case_code_WHEN_decode_THEN_it_is_the_same_locale() {
        assertEquals("Hello", decode("EN_US: Hello").translationOrNull(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_same_language_twice_in_different_case_WHEN_decode_THEN_last_one_wins() {
        assertEquals("b", decode("ru_ru: a\nRU_RU: b").translationOrNull(MinecraftLocales.RU_RU))
    }

    @Test
    fun GIVEN_null_WHEN_decode_THEN_text_is_empty() {
        assertEquals(emptyText, decode("null"))
    }

    @Test
    fun GIVEN_empty_map_WHEN_decode_THEN_text_is_empty() {
        assertEquals(emptyText, decode("{}"))
    }

    @Test
    fun GIVEN_list_WHEN_decode_THEN_error_names_the_path() {
        val error = assertFailsWith<SerializationException> { decode("- a") }

        assertTrue(error.message.orEmpty().contains("Expected a text or a map"))
    }

    @Test
    fun GIVEN_nested_map_as_translation_WHEN_decode_THEN_error_names_the_language() {
        val error = assertFailsWith<SerializationException> { decode("ru_ru:\n  a: b") }

        assertTrue(error.message.orEmpty().contains("ru_ru"))
    }

    @Test
    fun GIVEN_shared_text_only_WHEN_encode_THEN_it_is_written_as_scalar() {
        assertEquals("\"Hi\"", encode(LocalizedText.shared("Hi")))
    }

    @Test
    fun GIVEN_translations_and_shared_text_WHEN_encode_and_decode_THEN_value_is_the_same() {
        val value = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "Привет")
            translation(MinecraftLocales.EN_US, "Hello")
            shared("Hi")
        }

        val encoded = encode(value)

        val expected = """
            "ru_ru": "Привет"
            "en_us": "Hello"
            "*": "Hi"
        """.trimIndent()
        assertEquals(expected, encoded)
        assertEquals(value, decode(encoded))
    }

    @Test
    fun GIVEN_empty_text_WHEN_encode_and_decode_THEN_value_is_the_same() {
        assertEquals(emptyText, decode(encode(emptyText)))
    }

    @Test
    fun GIVEN_format_other_than_yaml_WHEN_decode_THEN_string_is_shared_text() {
        val decoded = Json.decodeFromString(LocalizedTextSerializer, "\"Hi\"")

        assertEquals(LocalizedText.shared("Hi"), decoded)
    }

    @Test
    fun GIVEN_locale_with_language_only_WHEN_encode_THEN_code_has_no_country() {
        val value = LocalizedText.build { translation(Locale.of("enws"), "Thee") }

        assertEquals(""""enws": "Thee"""", encode(value))
        assertEquals(value, decode(encode(value)))
    }

    @Test
    fun GIVEN_numeric_scalar_WHEN_decode_THEN_it_is_kept_as_text() {
        assertEquals(LocalizedText.shared("42"), decode("42"))
    }
}

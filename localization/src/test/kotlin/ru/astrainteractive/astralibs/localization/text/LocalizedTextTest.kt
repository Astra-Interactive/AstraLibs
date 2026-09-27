@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.localization.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocales
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalizedTextTest {
    private val german = Locale.of("de", "DE")
    private val britishEnglish = Locale.of("en", "GB")

    private val greeting = LocalizedText.build {
        translation(MinecraftLocales.RU_RU, "Привет")
        translation(MinecraftLocales.EN_US, "Hello")
    }

    /** Flattens a tree to `§` codes, so differently nested trees with the same look compare equal. */
    private val visualForm = LegacyComponentSerializer.legacySection()

    private fun plainText(component: Component): String {
        return PlainTextComponentSerializer.plainText().serialize(component)
    }

    @Test
    fun GIVEN_translation_for_exact_locale_WHEN_resolve_THEN_returns_it() {
        assertEquals("Hello", greeting.resolve(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_translation_for_other_country_of_same_language_WHEN_resolve_THEN_returns_it() {
        assertEquals("Hello", greeting.resolve(britishEnglish))
    }

    @Test
    fun GIVEN_no_translation_and_shared_text_WHEN_resolve_THEN_shared_text_wins_over_first_translation() {
        val text = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "Привет")
            shared("Hi")
        }

        assertEquals("Hi", text.resolve(german))
    }

    @Test
    fun GIVEN_no_translation_and_no_shared_text_WHEN_resolve_THEN_returns_first_translation() {
        assertEquals("Привет", greeting.resolve(german))
    }

    @Test
    fun GIVEN_translations_listed_in_other_order_WHEN_resolve_unknown_language_THEN_first_listed_wins() {
        val text = LocalizedText.build {
            translation(MinecraftLocales.EN_US, "Hello")
            translation(MinecraftLocales.RU_RU, "Привет")
        }

        assertEquals("Hello", text.resolve(german))
    }

    @Test
    fun GIVEN_unknown_receiver_locale_WHEN_resolve_THEN_returns_first_translation() {
        assertEquals("Привет", greeting.resolve(Locale.ROOT))
    }

    @Test
    fun GIVEN_text_without_languages_WHEN_resolve_THEN_returns_null() {
        val text = LocalizedText(sharedText = null, translationByLocale = emptyMap())

        assertNull(text.resolve(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_text_without_languages_WHEN_render_THEN_renders_empty_component() {
        val text = LocalizedText(sharedText = null, translationByLocale = emptyMap())

        assertEquals(Component.empty(), text.toComponent(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_receiver_language_WHEN_render_THEN_renders_its_translation() {
        assertEquals("Hello", plainText(greeting.toComponent(MinecraftLocales.EN_US)))
    }

    @Test
    fun GIVEN_legacy_codes_and_mini_message_tags_WHEN_render_THEN_both_are_parsed() {
        val text = LocalizedText.shared("&aHi <red>there")

        assertEquals("§aHi §cthere", visualForm.serialize(text.toComponent(MinecraftLocales.EN_US)))
    }

    @Test
    fun GIVEN_shared_prefix_WHEN_concat_with_translations_THEN_every_translation_gets_prefix() {
        val prefix = LocalizedText.shared("[P] ")

        val joined = prefix.concat(greeting)

        val expected = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "[P] Привет")
            translation(MinecraftLocales.EN_US, "[P] Hello")
        }
        assertEquals(expected, joined)
    }

    @Test
    fun GIVEN_two_shared_texts_WHEN_concat_THEN_shared_texts_are_joined() {
        val joined = LocalizedText.shared("a").concat(LocalizedText.shared("b"))

        assertEquals(LocalizedText.shared("ab"), joined)
    }

    @Test
    fun GIVEN_language_only_one_side_has_WHEN_concat_THEN_language_reads_default_language_of_joined_text() {
        val second = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "!")
            translation(MinecraftLocales.EN_US, "!")
            translation(german, "!")
        }

        val joined = greeting.concat(second)

        assertNull(joined.translationOrNull(german))
        assertEquals("Привет!", plainText(joined.toComponent(german)))
    }

    @Test
    fun GIVEN_language_only_first_side_has_WHEN_concat_THEN_language_is_left_out() {
        val first = LocalizedText.build {
            translation(MinecraftLocales.EN_US, "Hello")
            translation(german, "Hallo")
        }
        val second = LocalizedText.build { translation(MinecraftLocales.EN_US, "!") }

        val joined = first.concat(second)

        assertNull(joined.translationOrNull(german))
        assertEquals("Hello!", joined.translationOrNull(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_other_country_of_same_language_WHEN_concat_THEN_both_sides_are_joined() {
        val first = LocalizedText.build { translation(MinecraftLocales.EN_US, "Hello") }
        val second = LocalizedText.build { translation(britishEnglish, ", mate") }

        val joined = first.concat(second)

        assertEquals("Hello, mate", joined.translationOrNull(britishEnglish))
        assertEquals("Hello, mate", joined.translationOrNull(MinecraftLocales.EN_US))
    }

    @Test
    fun GIVEN_legacy_code_in_prefix_WHEN_concat_and_render_THEN_code_keeps_applying_to_message() {
        val joined = LocalizedText.shared("&a").concat(LocalizedText.shared("Hi"))

        assertEquals("§aHi", visualForm.serialize(joined.toComponent(MinecraftLocales.EN_US)))
    }

    @Test
    fun GIVEN_repeated_locale_WHEN_build_THEN_last_translation_wins() {
        val text = LocalizedText.build {
            translation(MinecraftLocales.EN_US, "first")
            translation(MinecraftLocales.EN_US, "second")
        }

        assertEquals("second", text.translationOrNull(MinecraftLocales.EN_US))
    }
}

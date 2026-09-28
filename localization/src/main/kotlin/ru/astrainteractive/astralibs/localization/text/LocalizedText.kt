package ru.astrainteractive.astralibs.localization.text

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.localization.component.LocalizableComponent
import ru.astrainteractive.astralibs.localization.markup.AutoComponentSerializer
import java.util.Locale

/**
 * Markup of one text in several languages, as it is written in a configuration file. The markup is parsed by
 * [AutoComponentSerializer], so MiniMessage tags and legacy `&` codes can be mixed.
 *
 * @property sharedText the text for every language that has no translation of its own
 * @property translationByLocale translations keyed by the client language they are written for. Without
 * [sharedText], the first one is the default language of the text.
 */
@Serializable(with = LocalizedTextSerializer::class)
data class LocalizedText(
    val sharedText: String?,
    val translationByLocale: Map<Locale, String>
) : LocalizableComponent {
    /** Looks up [locale] exactly, then any locale of the same language, so `en_gb` takes `en_us`. */
    fun translationOrNull(locale: Locale): String? {
        return translationByLocale[locale]
            ?: translationByLocale.entries
                .firstOrNull { entry -> entry.key.language == locale.language }
                ?.value
    }

    private fun textForConcat(locale: Locale): String? = translationOrNull(locale) ?: sharedText

    /**
     * The translation for [locale], then [sharedText], then the first translation, so a language nobody wrote
     * for and the console's [Locale.ROOT] read the default language of the text.
     *
     * @return null only when the text has no language at all
     */
    fun resolve(locale: Locale): String? {
        return translationOrNull(locale) ?: sharedText ?: translationByLocale.values.firstOrNull()
    }

    /** Renders [Component.empty] when the text has no language at all. */
    override fun toComponent(locale: Locale): Component {
        val text = resolve(locale) ?: return Component.empty()
        return AutoComponentSerializer.toComponent(text)
    }

    /**
     * Joins the markup of both texts as strings, so a `&` code of this text keeps applying to [other], e.g. a
     * prefix in front of a default message.
     *
     * A language is kept only when both texts can supply it, from a translation or a shared text. Any other
     * language is left out and reads the default language of the joined text, instead of mixing two languages
     * in one line.
     */
    fun concat(other: LocalizedText): LocalizedText {
        val joinedSharedText = if (sharedText != null && other.sharedText != null) {
            sharedText + other.sharedText
        } else {
            null
        }
        val locales = translationByLocale.keys + other.translationByLocale.keys
        val joinedTranslationByLocale = buildMap {
            locales.forEach { locale ->
                val first = textForConcat(locale) ?: return@forEach
                val second = other.textForConcat(locale) ?: return@forEach
                put(locale, first + second)
            }
        }
        return LocalizedText(
            sharedText = joinedSharedText,
            translationByLocale = joinedTranslationByLocale
        )
    }

    companion object {
        /**
         * Builds a text with a translation per language:
         *
         * ```kotlin
         * LocalizedText.build {
         *     translation(MinecraftLocales.RU_RU, "Привет")
         *     translation(MinecraftLocales.EN_US, "Hello")
         * }
         * ```
         */
        fun build(block: LocalizedTextBuilder.() -> Unit): LocalizedText {
            return LocalizedTextBuilder()
                .apply(block)
                .build()
        }

        /** Builds a text that is the same in every language, such as a prefix. */
        fun shared(text: String): LocalizedText {
            return LocalizedText(
                sharedText = text,
                translationByLocale = emptyMap()
            )
        }
    }
}

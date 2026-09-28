package ru.astrainteractive.astralibs.localization.text

import java.util.Locale

/** @see LocalizedText.build */
class LocalizedTextBuilder {
    private var sharedText: String? = null
    private val translationByLocale = LinkedHashMap<Locale, String>()

    /** Sets the text for every language that has no translation of its own. */
    fun shared(text: String) {
        sharedText = text
    }

    /** A repeated [locale] replaces the earlier translation. */
    fun translation(locale: Locale, text: String) {
        translationByLocale[locale] = text
    }

    fun build(): LocalizedText {
        return LocalizedText(
            sharedText = sharedText,
            translationByLocale = translationByLocale.toMap()
        )
    }
}

package ru.astrainteractive.astralibs.localization.text

import java.util.Locale

/** @see LocalizedText.build */
public class LocalizedTextBuilder {
    private var sharedText: String? = null
    private val translationByLocale = LinkedHashMap<Locale, String>()

    /** Sets the text for every language that has no translation of its own. */
    public fun shared(text: String) {
        sharedText = text
    }

    /** A repeated [locale] replaces the earlier translation. */
    public fun translation(locale: Locale, text: String) {
        translationByLocale[locale] = text
    }

    public fun build(): LocalizedText {
        return LocalizedText(
            sharedText = sharedText,
            translationByLocale = translationByLocale.toMap()
        )
    }
}

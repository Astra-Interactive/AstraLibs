package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import java.util.Locale

/** Text whose [Component] is built only once the language of its receiver is known. */
fun interface LocalizableComponent {
    /** @param locale language of the receiver; [Locale.ROOT] when it is unknown, as for the console */
    fun toComponent(locale: Locale): Component
}

/**
 * Places [other] after this one as a sibling, so the style of this component does not leak into [other].
 * This is the only `plus` for localizable components; joining the markup of two texts as strings is
 * [ru.astrainteractive.astralibs.localization.text.LocalizedText.concat].
 */
operator fun LocalizableComponent.plus(other: LocalizableComponent): LocalizableComponent {
    return JoinedLocalizableComponent(
        first = this,
        second = other
    )
}

/**
 * Replaces [replacements] in one pass after the markup is parsed, rendering each value in the same language.
 * Use it whenever more than one value comes from players: no value is searched for the other placeholders.
 *
 * Placeholders inside hover texts are replaced; placeholders inside click actions are not. A placeholder split
 * by formatting, like `%pla&ayer%`, is not found. Empty placeholders are skipped.
 */
fun LocalizableComponent.replaceAll(replacements: List<PlaceholderReplacement>): LocalizableComponent {
    val applicableReplacements = replacements.filter { replacement -> replacement.placeholder.isNotEmpty() }
    if (applicableReplacements.isEmpty()) return this
    return ReplacedLocalizableComponent(
        template = this,
        replacements = applicableReplacements
    )
}

/** @see replaceAll */
fun LocalizableComponent.replaceAll(vararg replacements: PlaceholderReplacement): LocalizableComponent {
    return replaceAll(replacements.toList())
}

/**
 * Replaces one [placeholder] in a pass of its own. Chained calls search what earlier calls inserted, like chained
 * [String.replace]; use [replaceAll] to insert several player values safely.
 */
fun LocalizableComponent.replace(placeholder: String, value: LocalizableComponent): LocalizableComponent {
    val replacement = PlaceholderReplacement(
        placeholder = placeholder,
        value = value
    )
    return replaceAll(replacement)
}

/** Inserts [value] as plain text: its `&` codes and tags stay literal, so player input is safe here. */
fun LocalizableComponent.replace(placeholder: String, value: String): LocalizableComponent {
    return replaceAll(PlaceholderReplacement.plain(placeholder, value))
}

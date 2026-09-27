package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

/**
 * Replaces all [replacements] of one call in a single pass: a value is never searched for placeholders again,
 * and when a placeholder is listed twice the first value wins.
 */
internal class ReplacedLocalizableComponent(
    private val template: LocalizableComponent,
    private val replacements: List<PlaceholderReplacement>
) : LocalizableComponent {
    private val uniqueReplacements = replacements.distinctBy { replacement -> replacement.placeholder }

    /** Longer placeholders go first, so `%player%` does not stop `%player_name%` from matching. */
    private val placeholderPattern: Pattern = uniqueReplacements
        .map { replacement -> replacement.placeholder }
        .sortedByDescending { placeholder -> placeholder.length }
        .joinToString(separator = "|") { placeholder -> Pattern.quote(placeholder) }
        .let { regex -> patternByRegex.computeIfAbsent(regex, Pattern::compile) }

    override fun toComponent(locale: Locale): Component {
        val valueByPlaceholder = uniqueReplacements.associateBy(
            keySelector = { replacement -> replacement.placeholder },
            valueTransform = { replacement -> replacement.value.toComponent(locale) }
        )
        return template.toComponent(locale).replaceText { builder ->
            builder
                .match(placeholderPattern)
                .replacement { match, _ -> valueByPlaceholder[match.group()] }
        }
    }

    companion object {
        /** Placeholders are constants of the calling code, so the set of patterns stays small. */
        private val patternByRegex = ConcurrentHashMap<String, Pattern>()
    }
}

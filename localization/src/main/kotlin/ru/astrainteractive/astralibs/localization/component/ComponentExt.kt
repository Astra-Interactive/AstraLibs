package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration

/** Returns this component if non-null, or [Component.empty]. */
fun Component?.orEmpty() = this ?: Component.empty()

/** Returns `true` if this component equals [Component.empty]. */
fun Component.isEmpty() = this == Component.empty()

/** Returns `true` if this component does not equal [Component.empty]. */
fun Component.isNotEmpty() = this != Component.empty()

/** Attaches a callback click event that invokes [onClick] when the component is clicked in chat. */
fun Component.clickable(onClick: (Audience) -> Unit): Component {
    return clickEvent(
        ClickEvent.callback { audience ->
            onClick.invoke(audience)
        }
    )
}

/** Item names and lore render italic unless told otherwise, while config text is meant to be upright. */
internal fun Component.withItalicOffByDefault(): Component {
    return decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

/** A ready component that looks the same in every language. */
fun Component.asLocalizableComponent(): LocalizableComponent {
    return LocalizableComponent { _ -> this }
}

/**
 * Places [other] after this one as a sibling, so the style of this component does not leak into [other].
 * This is the only `plus` for localizable components; joining the markup of two texts as strings is
 * [ru.astrainteractive.astralibs.localization.text.LocalizedText.concat].
 */
operator fun LocalizableComponent.plus(other: LocalizableComponent): LocalizableComponent {
    val first = this
    return LocalizableComponent { locale ->
        Component.textOfChildren(first.toComponent(locale), other.toComponent(locale))
    }
}

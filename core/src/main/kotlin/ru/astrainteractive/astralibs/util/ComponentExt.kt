package ru.astrainteractive.astralibs.util

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent

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

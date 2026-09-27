package ru.astrainteractive.astralibs.localization.markup

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.ParsingException
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * Parses MiniMessage tags first, then legacy `&` codes inside each text part; a `&` code applies until the next tag.
 * JSON is never parsed, so player text like `{text:hi}` cannot turn into a component.
 *
 * - A string MiniMessage rejects, such as one with `§` codes, is parsed as legacy only.
 * - Text under a `<click>` is not turned into links, so the tag keeps its own action.
 * - `&` codes stay literal inside `<gradient>`, `<rainbow>` and `<pride>` (they split text per character),
 *   in `<lang>` arguments and in entity hover names; `&r` does not end an enclosing tag.
 * - Escape player text with `MiniMessage.escapeTags` before splicing it into a string.
 */
data object AutoComponentSerializer :
    KyoriComponentSerializer,
    ComponentSerializer<Component, Component, String> {
    private val legacyWithoutLinks by lazy {
        LegacyComponentSerializer.legacyAmpersand()
    }

    override val type: KyoriComponentSerializerType = KyoriComponentSerializerType.Auto

    /** This object itself, so `serializer.deserialize` parses exactly like [toComponent]. */
    override val serializer: ComponentSerializer<Component, out Component, String>
        get() = this

    private fun Component.withLegacyHoverText(): Component {
        val hoverText = hoverEvent()
            ?.takeIf { event -> event.action() == HoverEvent.Action.SHOW_TEXT }
            ?.value() as? Component
            ?: return this
        val legacyHoverText = hoverText.withLegacyCodes(isInsideClick = false).compact()
        return hoverEvent(HoverEvent.showText(legacyHoverText))
    }

    /** Tag arguments such as a click URL are not text nodes, so their `&` characters are never parsed. */
    private fun Component.withLegacyCodes(isInsideClick: Boolean): Component {
        val isClickable = isInsideClick || clickEvent() != null
        val children = children().map { child -> child.withLegacyCodes(isClickable) }
        val component = withLegacyHoverText()
        if (component !is TextComponent) return component.children(children)
        // Raw parsers: Legacy.toComponent turns italic off, which would override an enclosing <italic>
        val legacy = if (isClickable) legacyWithoutLinks else KyoriComponentSerializer.Legacy.serializer
        return Component.text()
            .style(component.style())
            .append(legacy.deserialize(component.content()))
            .append(children)
            .build()
    }

    /** @return null when MiniMessage rejects the string, e.g. because of legacy `§` codes */
    private fun deserializeMiniMessage(string: String): Component? {
        return runCatching { KyoriComponentSerializer.MiniMessage.serializer.deserialize(string) }
            .onFailure { error -> if (error !is ParsingException) throw error }
            .getOrNull()
    }

    override fun toComponent(string: String): Component {
        val miniMessageComponent = deserializeMiniMessage(string)
            ?: return KyoriComponentSerializer.Legacy.toComponent(string)
        return miniMessageComponent
            .withLegacyCodes(isInsideClick = false)
            .compact()
            .withItalicOffByDefault()
    }

    override fun deserialize(input: String): Component = toComponent(input)

    /**
     * Writes MiniMessage. Text that contains `&` or `§` codes (`R&D`) or looks like a link (`10.50`) is not read
     * back as it was: legacy codes have no escape, and link-looking text becomes a link.
     */
    override fun serialize(component: Component): String {
        return KyoriComponentSerializer.MiniMessage.serializer.serialize(component)
    }
}

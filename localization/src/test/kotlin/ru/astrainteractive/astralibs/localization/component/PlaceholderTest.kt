@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocales
import ru.astrainteractive.astralibs.localization.text.LocalizedText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PlaceholderTest {
    private val locale = MinecraftLocales.EN_US

    /** Flattens a tree to `§` codes, so differently nested trees with the same look compare equal. */
    private val visualForm = LegacyComponentSerializer.legacySection()

    private fun plainText(component: LocalizableComponent): String {
        return PlainTextComponentSerializer.plainText().serialize(component.toComponent(locale))
    }

    private fun selfAndDescendants(component: Component): List<Component> {
        return listOf(component) + component.children().flatMap(::selfAndDescendants)
    }

    @Test
    fun GIVEN_player_value_with_markup_WHEN_replace_THEN_markup_stays_literal() {
        val message = LocalizedText.shared("&aHi %player%").replace("%player%", "&c<red>Steve")

        assertEquals("§aHi &c<red>Steve", visualForm.serialize(message.toComponent(locale)))
    }

    @Test
    fun GIVEN_value_containing_other_placeholder_WHEN_replace_all_THEN_value_is_not_searched_again() {
        val message = LocalizedText.shared("%player%: %message%").replaceAll(
            PlaceholderReplacement.plain("%player%", "Steve"),
            PlaceholderReplacement.plain("%message%", "hi %player%")
        )

        assertEquals("Steve: hi %player%", plainText(message))
    }

    @Test
    fun GIVEN_chained_replace_WHEN_rendered_THEN_later_call_searches_what_earlier_call_inserted() {
        val message = LocalizedText.shared("%message% from %player%")
            .replace("%message%", "hi %player%")
            .replace("%player%", "Steve")

        assertEquals("hi Steve from Steve", plainText(message))
    }

    @Test
    fun GIVEN_placeholder_listed_twice_WHEN_replace_all_THEN_first_value_wins() {
        val message = LocalizedText.shared("%a%").replaceAll(
            PlaceholderReplacement.plain("%a%", "1"),
            PlaceholderReplacement.plain("%a%", "2")
        )

        assertEquals("1", plainText(message))
    }

    @Test
    fun GIVEN_placeholder_that_prefixes_another_WHEN_replace_all_THEN_longer_one_matches_first() {
        val message = LocalizedText.shared("%player% / %player_name%").replaceAll(
            listOf(
                PlaceholderReplacement.plain("%player%", "A"),
                PlaceholderReplacement.plain("%player_name%", "B")
            )
        )

        assertEquals("A / B", plainText(message))
    }

    @Test
    fun GIVEN_localizable_value_WHEN_replace_THEN_value_is_rendered_in_same_language() {
        val flag = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "Строить")
            translation(MinecraftLocales.EN_US, "Build")
        }

        val message = LocalizedText.shared("Flag: %flag%").replace("%flag%", flag)

        assertEquals("Flag: Build", plainText(message))
    }

    @Test
    fun GIVEN_placeholder_in_hover_and_click_WHEN_replace_THEN_only_hover_is_replaced() {
        val template = LocalizedText.shared(
            "<hover:show_text:'by %player%'><click:run_command:'/msg %player%'>[reply]</click></hover>"
        )

        val rendered = template.replace("%player%", "Steve").toComponent(locale)

        val nodes = selfAndDescendants(rendered)
        val hoverText = nodes.firstNotNullOf { node -> node.hoverEvent() }.value() as Component
        val clickEvent = nodes.firstNotNullOf { node -> node.clickEvent() }
        assertEquals("by Steve", PlainTextComponentSerializer.plainText().serialize(hoverText))
        assertEquals(HoverEvent.Action.SHOW_TEXT, nodes.firstNotNullOf { node -> node.hoverEvent() }.action())
        assertEquals(ClickEvent.runCommand("/msg %player%"), clickEvent)
    }

    @Test
    fun GIVEN_empty_placeholder_WHEN_replace_THEN_component_is_returned_unchanged() {
        val text = LocalizedText.shared("a%b")

        assertSame(text, text.replace("", "z"))
    }

    @Test
    fun GIVEN_no_replacements_WHEN_replace_all_THEN_component_is_returned_unchanged() {
        val text = LocalizedText.shared("a")

        assertSame(text, text.replaceAll(emptyList()))
    }

    @Test
    fun GIVEN_placeholder_absent_from_text_WHEN_replace_THEN_text_is_unchanged() {
        assertEquals("plain", plainText(LocalizedText.shared("plain").replace("%p%", "x")))
    }
}

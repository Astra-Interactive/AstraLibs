@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.kyori

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import ru.astrainteractive.astralibs.string.StringDesc

class AutoComponentSerializerTest {

    private val miniMessage = MiniMessage.miniMessage()

    /** Flattens a tree to `§` codes, so differently nested trees with the same look compare equal. */
    private val visualForm = LegacyComponentSerializer.builder().hexColors().build()

    private fun auto(string: String): Component {
        return AutoComponentSerializer.toComponent(string)
    }

    private fun renderAuto(string: String): String {
        return visualForm.serialize(auto(string))
    }

    private fun renderLegacy(string: String): String {
        return visualForm.serialize(KyoriComponentSerializer.Legacy.toComponent(string))
    }

    private fun renderMiniMessage(string: String): String {
        return visualForm.serialize(miniMessage.deserialize(string))
    }

    private fun plainText(component: Component): String {
        return PlainTextComponentSerializer.plainText().serialize(component)
    }

    private fun Component.selfAndDescendants(): List<Component> {
        return listOf(this) + children().flatMap { child -> child.selfAndDescendants() }
    }

    private fun clickEventsOf(component: Component): List<ClickEvent<*>> {
        return component.selfAndDescendants().mapNotNull { node -> node.clickEvent() }
    }

    private fun hoverTextsOf(component: Component): List<Component> {
        return component.selfAndDescendants().mapNotNull { node -> node.hoverEvent()?.value() as? Component }
    }

    /** JSON of the compacted tree: the full content, style, click and hover of a component. */
    private fun assertSameComponent(expected: Component, actual: Component, message: String) {
        val gson = GsonComponentSerializer.gson()
        assertEquals(gson.serialize(expected.compact()), gson.serialize(actual.compact()), message)
    }

    private fun serializeAndParseBack(component: Component): Component {
        val serializer = AutoComponentSerializer.serializer
        return serializer.deserialize(serializer.serialize(component))
    }

    @Test
    fun GIVEN_string_without_tags_WHEN_to_component_THEN_renders_like_legacy() {
        listOf(
            "&7[&#DBB72BRTP&7] &aНайдено место!",
            "&x&d&b&b&7&2&bНАГРАДА",
            "a < b <3",
            "<notatag> &aHi"
        ).forEach { string ->
            assertEquals(renderLegacy(string), renderAuto(string), string)
        }
    }

    @Test
    fun GIVEN_minimessage_tags_WHEN_to_component_THEN_tags_are_applied() {
        val string = "<gradient:red:blue>Привет</gradient> <bold>мир</bold>"

        assertEquals(renderMiniMessage(string), renderAuto(string))
    }

    @Test
    fun GIVEN_legacy_code_before_tag_WHEN_to_component_THEN_code_applies_until_tag() {
        val string = "&7[RTP] <bold>Найдено</bold>"

        assertEquals(renderMiniMessage("<gray>[RTP] </gray><bold>Найдено</bold>"), renderAuto(string))
    }

    @Test
    fun GIVEN_legacy_code_inside_tag_WHEN_to_component_THEN_tag_style_is_kept() {
        val string = "<bold>&aНайдено</bold>"

        assertEquals(renderMiniMessage("<bold><green>Найдено</green></bold>"), renderAuto(string))
    }

    @Test
    fun GIVEN_legacy_code_next_to_keybind_tag_WHEN_to_component_THEN_code_is_parsed() {
        val string = "<key:key.jump> &aпрыжок"

        assertEquals(renderMiniMessage("<key:key.jump> <green>прыжок"), renderAuto(string))
    }

    @Test
    fun GIVEN_ampersand_in_click_url_WHEN_to_component_THEN_url_is_not_altered() {
        val string = "<click:open_url:'https://example.com/vote?a=1&ref=2'>Голосовать</click>"

        assertEquals(listOf(ClickEvent.openUrl("https://example.com/vote?a=1&ref=2")), clickEventsOf(auto(string)))
    }

    @Test
    fun GIVEN_legacy_code_in_hover_text_WHEN_to_component_THEN_hover_text_is_parsed() {
        val string = "<hover:show_text:'&aподсказка'>текст</hover>"

        val hoverTexts = hoverTextsOf(auto(string)).map { hoverText -> visualForm.serialize(hoverText) }

        assertEquals(listOf(renderMiniMessage("<green>подсказка")), hoverTexts)
    }

    @Test
    fun GIVEN_show_item_hover_WHEN_to_component_THEN_hover_is_kept() {
        val string = "<hover:show_item:stone>&aкамень</hover>"

        val hoverActions = auto(string).selfAndDescendants().mapNotNull { node -> node.hoverEvent()?.action() }

        assertEquals(listOf<HoverEvent.Action<*>>(HoverEvent.Action.SHOW_ITEM), hoverActions)
    }

    @Test
    fun GIVEN_url_like_text_inside_run_command_click_WHEN_to_component_THEN_only_command_click_exists() {
        val string = "<click:run_command:'/buy'>Купить за 10.50 на spawn.world</click>"

        assertEquals(listOf(ClickEvent.runCommand("/buy")), clickEventsOf(auto(string)))
    }

    @Test
    fun GIVEN_url_outside_click_tag_WHEN_to_component_THEN_url_is_clickable() {
        val string = "<bold>Сайт: https://example.com</bold>"

        assertTrue(ClickEvent.openUrl("https://example.com") in clickEventsOf(auto(string)))
    }

    @Test
    fun GIVEN_json_shaped_player_text_WHEN_to_component_THEN_braces_are_kept_as_text() {
        listOf("{ }", "{}", "{text:hi}", "{привет, как дела}", """  {"text":"hi"}  """).forEach { string ->
            assertEquals(string, plainText(auto(string)), string)
        }
    }

    @Test
    fun GIVEN_json_component_with_click_event_WHEN_to_component_THEN_no_click_event_is_created() {
        val string = """{"text":"free","click_event":{"action":"run_command","command":"/pay thief 1000"}}"""

        assertEquals(emptyList<ClickEvent<*>>(), clickEventsOf(auto(string)))
    }

    @Test
    fun GIVEN_section_sign_code_with_tags_WHEN_to_component_THEN_renders_like_legacy() {
        val string = "§a<red>Hi"

        assertEquals(renderLegacy(string), renderAuto(string))
    }

    @Test
    fun GIVEN_section_sign_before_kelvin_sign_WHEN_to_component_THEN_renders_like_legacy() {
        val string = "Привет §Kмир <red>!"

        assertEquals(renderLegacy(string), renderAuto(string))
    }

    @Test
    fun GIVEN_italic_tag_around_whole_string_WHEN_to_component_THEN_text_is_italic() {
        val string = "<italic>Вся строка</italic>"

        assertEquals(renderMiniMessage(string), renderAuto(string))
    }

    @Test
    fun GIVEN_no_italic_markup_WHEN_to_component_THEN_italic_is_disabled() {
        assertEquals(TextDecoration.State.FALSE, auto("<red>Hi").decoration(TextDecoration.ITALIC))
    }

    @Test
    fun GIVEN_plain_desc_with_markup_WHEN_to_component_THEN_text_is_not_parsed_and_italic_is_disabled() {
        val component = AutoComponentSerializer.toComponent(StringDesc.Plain("&a<red>Hi"))

        assertEquals("&a<red>Hi", plainText(component))
        assertEquals(TextDecoration.State.FALSE, component.decoration(TextDecoration.ITALIC))
    }

    @Test
    fun GIVEN_nesting_deeper_than_the_stack_WHEN_to_component_THEN_error_is_not_hidden() {
        assertFailsWith<StackOverflowError> { auto("<b>".repeat(100_000)) }
    }

    @Test
    fun GIVEN_legacy_code_inside_color_changing_tag_WHEN_to_component_THEN_code_stays_text() {
        assertEquals("&lНазвание", plainText(auto("<gradient:red:blue>&lНазвание</gradient>")))
        assertEquals("&aНазвание", plainText(auto("<rainbow>&aНазвание</rainbow>")))
        assertEquals("&aНазвание", plainText(auto("<pride>&aНазвание</pride>")))
    }

    @Test
    fun GIVEN_string_WHEN_serializer_deserializes_THEN_result_equals_to_component() {
        listOf("&7[RTP] <bold>Найдено</bold>", "&aЗелёный", "<red>Hi", "§a<red>Hi").forEach { string ->
            assertSameComponent(auto(string), AutoComponentSerializer.serializer.deserialize(string), string)
        }
    }

    @Test
    fun GIVEN_string_WHEN_parsed_serialized_and_parsed_again_THEN_component_is_the_same() {
        listOf(
            "&7[&#DBB72BRTP&7] &aНайдено место!",
            "&7[RTP] <bold>Найдено</bold>",
            "<gradient:red:blue>Привет</gradient> <bold>мир</bold>",
            "<hover:show_text:'&aподсказка'>текст</hover>",
            "<click:open_url:'https://example.com/?a=1&ref=2'>сайт</click>",
            "<click:run_command:'/buy'>Купить за 10.50</click>",
            "Tom & Jerry, a < b, {text:hi}"
        ).forEach { string ->
            val component = auto(string)

            assertSameComponent(component, serializeAndParseBack(component), string)
        }
    }

    @Test
    fun GIVEN_component_WHEN_serialized_and_parsed_back_THEN_component_is_the_same() {
        listOf(
            Component.text("Привет", NamedTextColor.RED),
            Component.text()
                .append(Component.text("жирный").decorate(TextDecoration.BOLD))
                .append(Component.text(" обычный"))
                .build(),
            Component.text("клик").clickEvent(ClickEvent.runCommand("/spawn")),
            Component.text("наведи").hoverEvent(HoverEvent.showText(Component.text("подсказка", NamedTextColor.GREEN))),
            Component.text("Tom & Jerry, a < b, {text:hi}")
        ).forEach { component ->
            val expected = component.withItalicOffByDefault()

            assertSameComponent(expected, serializeAndParseBack(component), component.toString())
        }
    }

    @Test
    fun GIVEN_auto_type_WHEN_of_type_THEN_returns_auto_serializer() {
        assertEquals(
            AutoComponentSerializer,
            KyoriComponentSerializer.ofType(KyoriComponentSerializerType.Auto)
        )
    }
}

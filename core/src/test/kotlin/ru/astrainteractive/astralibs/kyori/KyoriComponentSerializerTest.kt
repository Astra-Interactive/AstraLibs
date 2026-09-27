@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.kyori

import kotlin.test.Test
import kotlin.test.assertEquals
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class KyoriComponentSerializerTest {

    /** Flattens a tree to `§` codes, so differently nested trees with the same look compare equal. */
    private val visualForm = LegacyComponentSerializer.builder().hexColors().build()

    @Test
    fun GIVEN_italic_code_at_start_WHEN_legacy_to_component_THEN_text_is_italic() {
        val component = KyoriComponentSerializer.Legacy.toComponent("&oКурсив")

        val expected = Component.text("Курсив").decorate(TextDecoration.ITALIC)
        assertEquals(visualForm.serialize(expected), visualForm.serialize(component))
    }

    @Test
    fun GIVEN_string_without_italic_code_WHEN_legacy_to_component_THEN_italic_is_disabled() {
        val component = KyoriComponentSerializer.Legacy.toComponent("&aHi")

        assertEquals(TextDecoration.State.FALSE, component.decoration(TextDecoration.ITALIC))
    }
}

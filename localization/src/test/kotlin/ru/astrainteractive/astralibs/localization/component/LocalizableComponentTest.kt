@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocales
import ru.astrainteractive.astralibs.localization.text.LocalizedText
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizableComponentTest {
    /** Flattens a tree to `§` codes, so differently nested trees with the same look compare equal. */
    private val visualForm = LegacyComponentSerializer.legacySection()

    @Test
    fun GIVEN_colored_first_part_WHEN_plus_THEN_its_color_does_not_leak_into_second_part() {
        val joined = LocalizedText.shared("&c[P] ") + LocalizedText.shared("text")

        assertEquals("§c[P] §rtext", visualForm.serialize(joined.toComponent(MinecraftLocales.EN_US)))
    }

    @Test
    fun GIVEN_localized_parts_WHEN_plus_THEN_each_part_is_rendered_in_receiver_language() {
        val greeting = LocalizedText.build {
            translation(MinecraftLocales.RU_RU, "Привет")
            translation(MinecraftLocales.EN_US, "Hello")
        }

        val joined = greeting + LocalizedText.shared("!")

        assertEquals("Hello!", visualForm.serialize(joined.toComponent(MinecraftLocales.EN_US)))
    }

    @Test
    fun GIVEN_constant_component_WHEN_rendered_in_any_language_THEN_it_is_unchanged() {
        val component = Component.text("same", NamedTextColor.GOLD)
        val constant = ConstantLocalizableComponent(component)

        assertEquals(component, constant.toComponent(MinecraftLocales.EN_US))
        assertEquals(component, constant.toComponent(Locale.ROOT))
    }
}

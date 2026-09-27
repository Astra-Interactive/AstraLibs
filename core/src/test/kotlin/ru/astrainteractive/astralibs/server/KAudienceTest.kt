@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.localization.locale.MinecraftLocales
import ru.astrainteractive.astralibs.localization.text.LocalizedText
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class KAudienceTest {
    private val greeting = LocalizedText.build {
        translation(MinecraftLocales.RU_RU, "Привет")
        translation(MinecraftLocales.EN_US, "Hello")
    }

    @Test
    fun GIVEN_receiver_with_language_WHEN_send_message_THEN_it_reads_its_own_language() {
        val player = FakeReceiver(MinecraftLocales.EN_US)

        player.sendMessage(greeting)

        assertEquals(listOf("Hello"), player.messages)
    }

    @Test
    fun GIVEN_receiver_with_unknown_language_WHEN_send_message_THEN_it_reads_default_language_of_text() {
        val console = FakeReceiver(Locale.ROOT)

        console.sendMessage(greeting)

        assertEquals(listOf("Привет"), console.messages)
    }

    @Test
    fun GIVEN_receivers_with_different_languages_WHEN_send_message_to_all_THEN_each_reads_its_own_language() {
        val english = FakeReceiver(MinecraftLocales.EN_US)
        val russian = FakeReceiver(MinecraftLocales.RU_RU)

        listOf(english, russian).sendMessage(greeting)

        assertEquals(listOf("Hello"), english.messages)
        assertEquals(listOf("Привет"), russian.messages)
    }
}

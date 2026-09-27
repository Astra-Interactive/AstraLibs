@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.server

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.server.util.asKAudience
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class AdventureKAudienceTest {
    @Test
    fun GIVEN_audience_reporting_locale_WHEN_adapted_THEN_receiver_has_that_locale() {
        val audience = FakeAudience(Pointers.builder().withStatic(Identity.LOCALE, Locale.GERMANY).build())

        assertEquals(Locale.GERMANY, audience.asKAudience().locale)
    }

    @Test
    fun GIVEN_audience_without_locale_like_console_WHEN_adapted_THEN_locale_is_unknown() {
        val audience = FakeAudience(Pointers.empty())

        assertEquals(Locale.ROOT, audience.asKAudience().locale)
    }

    @Test
    fun GIVEN_adapted_audience_WHEN_send_message_THEN_audience_receives_it() {
        val audience = FakeAudience(Pointers.empty())
        val message = Component.text("hi")

        audience.asKAudience().sendMessage(message)

        assertEquals<List<Component>>(listOf(message), audience.messages)
    }
}

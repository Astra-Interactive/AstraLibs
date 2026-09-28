@file:Suppress("FunctionNaming")

package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class OnlineKPlayerArgumentConverterTest {
    private val onlinePlayer = FakeOnlineKPlayer(name = "jeb_")
    private val converter = OnlineKPlayerArgumentConverter(
        platformServer = FakePlatformServer(
            onlinePlayers = listOf(onlinePlayer),
            offlinePlayers = listOf(FakeKPlayer(name = "Notch"))
        )
    )

    @Test
    fun GIVEN_player_who_is_online_WHEN_converted_by_name_THEN_returns_that_player() {
        assertSame(onlinePlayer, converter.transform("jeb_"))
    }

    @Test
    fun GIVEN_player_who_is_offline_WHEN_converted_THEN_fails_as_unknown_player() {
        val exception = assertFailsWith<NoPlayerException> { converter.transform("Notch") }

        assertEquals("Notch", exception.name)
    }

    @Test
    fun GIVEN_name_the_server_does_not_know_WHEN_converted_THEN_fails_as_unknown_player() {
        assertFailsWith<NoPlayerException> { converter.transform("Herobrine") }
    }
}

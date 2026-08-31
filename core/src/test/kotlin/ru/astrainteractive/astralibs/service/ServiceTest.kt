package ru.astrainteractive.astralibs.service

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class ServiceTest {

    @Test
    fun GIVEN_active_service_WHEN_reloaded_THEN_it_is_stopped_and_started_again() {
        val service = FakeService()
        service.onEnable()
        service.transitions.clear()

        service.onReload()

        assertContentEquals(listOf(Service.State.Idle, Service.State.Active), service.transitions)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_idle_service_WHEN_reloaded_THEN_it_ends_up_running() {
        val service = FakeService()

        service.onReload()

        assertEquals(Service.State.Active, service.state.value)
    }
}

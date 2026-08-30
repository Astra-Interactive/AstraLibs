package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class ServiceExtTest {

    @Test
    fun GIVEN_idle_service_WHEN_await_idle_THEN_it_returns_without_suspending() = runTest {
        val service = FakeService()

        service.awaitIdle()

        assertEquals(Service.State.Idle, service.state.value)
    }

    @Test
    fun GIVEN_active_service_WHEN_it_is_disabled_THEN_await_idle_resumes() = runTest {
        val service = FakeService()
        service.onEnable()
        var resumed = false
        backgroundScope.launch {
            service.awaitIdle()
            resumed = true
        }

        runCurrent()
        assertFalse(resumed, "awaitIdle resumed while the service was still active")

        service.onDisable()
        runCurrent()

        assertTrue(resumed)
    }
}

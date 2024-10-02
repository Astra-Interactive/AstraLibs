package ru.astrainteractive.astralibs.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ReloadableFlowTest {

    @Test
    fun testSharedFlow() = runBlocking {
        var i = 0
        println("#start")
        val reloadableFlow = flow { emit(i++) }
            .shareInReloadable(CoroutineScope(SupervisorJob() + Dispatchers.IO))
        println("#created")
        reloadableFlow.first().let { oldValue ->
            println("#loaded first value $oldValue")
            assertEquals(oldValue, reloadableFlow.first())
            println("#restarting")
            reloadableFlow.reload()
            reloadableFlow.filter { it != oldValue }.first().let { newValue ->
                assertNotEquals(oldValue, newValue)
                assertEquals(newValue, reloadableFlow.first())

                reloadableFlow.reload()
                reloadableFlow.filter { it != newValue }.first().let { mostNewValue ->
                    assertNotEquals(newValue, mostNewValue)
                    assertEquals(mostNewValue, reloadableFlow.first())
                }
            }
        }
    }

    @Test
    fun testStateFlow() = runBlocking {
        var i = 0
        val reloadableFlow = flow { emit(i++) }
            .stateInReloadable(CoroutineScope(SupervisorJob() + Dispatchers.IO), -1)

        assertEquals(reloadableFlow.value, -1)

        reloadableFlow.value.let { oldValue ->
            assertEquals(oldValue, reloadableFlow.value)
            reloadableFlow.reload()
            reloadableFlow.filter { it != oldValue }.first().let { newValue ->
                assertNotEquals(oldValue, newValue)
                assertEquals(newValue, reloadableFlow.value)

                reloadableFlow.reload()
                reloadableFlow.filter { it != newValue }.first().let { mostNewValue ->
                    assertNotEquals(newValue, mostNewValue)
                    assertEquals(mostNewValue, reloadableFlow.value)
                }
            }
        }
    }
}

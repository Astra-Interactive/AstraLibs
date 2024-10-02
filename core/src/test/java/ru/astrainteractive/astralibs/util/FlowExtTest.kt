package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import ru.astrainteractive.astralibs.util.FlowExt.mapCached
import java.io.Closeable
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlowExtTest {
    private class CloseableImpl<T>(val value: T) : Closeable {
        var isClosed = false
            private set

        override fun close() {
            isClosed = true
        }
    }

    @Test
    fun testCachedFlow() = runBlocking {
        val intStateFlow = MutableStateFlow(1)
        val cachedStateFlow = intStateFlow
            .mapCached<Int, CloseableImpl<Int>>(CoroutineScope(SupervisorJob() + Dispatchers.IO)) { value, old ->
                old?.close()
                CloseableImpl(value)
            }
        cachedStateFlow.first().let { currentCache ->
            assertFalse(currentCache.isClosed)
            intStateFlow.update { it + 1 }
            cachedStateFlow.first { it.value != currentCache.value }
            assertTrue(currentCache.isClosed)
            assertFalse(cachedStateFlow.first().isClosed)
        }
    }
}

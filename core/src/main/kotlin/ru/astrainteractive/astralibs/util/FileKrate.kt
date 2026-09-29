package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner

/**
 * A krate of a file whose [cachedStateFlow] is the value last read or saved: a [read] that fails leaves it as it
 * is instead of replacing it with the default.
 */
internal class FileKrate<T>(
    private val read: () -> Result<T>,
    private val write: (T) -> Unit,
    private val factory: ValueFactory<T>,
    lockOwner: LockOwner
) : StateFlowMutableKrate<T>, LockOwner by lockOwner {
    private val stateFlow = MutableStateFlow(read.invoke().getOrElse { _ -> factory.create() })

    override val cachedStateFlow: StateFlow<T> = stateFlow.asStateFlow()

    override val cachedValue: T
        get() = stateFlow.value

    private fun writeAndCache(value: T): T {
        write.invoke(value)
        stateFlow.value = value
        return value
    }

    override fun getValue(): T = lock.withLock {
        read.invoke().onSuccess { value -> stateFlow.value = value }
        stateFlow.value
    }

    override fun save(value: T) {
        lock.withLock { writeAndCache(value) }
    }

    override fun saveAndGet(block: (T) -> T): T = lock.withLock { writeAndCache(block.invoke(getValue())) }

    override fun save(block: (T) -> T) {
        saveAndGet(block)
    }

    override fun resetAndGet(): T = lock.withLock { writeAndCache(factory.create()) }

    override fun reset() {
        resetAndGet()
    }
}

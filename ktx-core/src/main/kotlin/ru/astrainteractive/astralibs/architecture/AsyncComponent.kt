package ru.astrainteractive.astralibs.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class AsyncComponent {
    private val closeableCoroutineScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.IO)

    protected val scope: CoroutineScope
        get() = closeableCoroutineScope

    fun clear() {
        closeableCoroutineScope.close()
    }
}
package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

object FlowExt {
    /**
     * Allows to map a flow and get in map history of previous mapped flow
     */
    inline fun <T, R> Flow<T>.mapCached(
        scope: CoroutineScope,
        started: SharingStarted = SharingStarted.Eagerly,
        replay: Int = 1,
        crossinline transform: suspend (value: T, previous: R?) -> R
    ): SharedFlow<R> = flow {
        var latest: R? = null
        this@mapCached.map {
            val current = transform.invoke(it, latest)
            emit(current)
            latest = current
        }.collect()
    }.shareIn(scope, started, replay)
}

package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object FlowExt {
    /**
     * Allows to map a flow and get in map history of previous mapped flow
     */
    inline fun <T, R> Flow<T>.mapCached(
        crossinline transform: suspend (value: T, previous: R?) -> R
    ): Flow<R> = flow {
        var latest: R? = null
        this@mapCached.map {
            val current = transform.invoke(it, latest)
            emit(current)
            latest = current
        }.collect()
    }
}

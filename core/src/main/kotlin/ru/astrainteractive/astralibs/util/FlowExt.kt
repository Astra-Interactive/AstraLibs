package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlin.coroutines.CoroutineContext

/**
 * Transforms a [Flow] by applying a suspend [transform] function that has access to the previously emitted value.
 *
 * The transformation result is cached internally and the transformed flow is shared as a [SharedFlow]
 * using the given [scope], [started] strategy, and [replay] size.
 *
 * This function is useful when the transformation depends on the previously emitted result.
 *
 * @param scope The [CoroutineScope] in which the shared flow is active.
 * @param started The [SharingStarted] strategy to control when sharing starts/stops.
 * @param dispatcher The [Dispatchers] on which flow will be running
 * @param replay The number of values replayed to new subscribers.
 * @param transform A suspend lambda that takes the current upstream value and the previous transformed value.
 * @return A [SharedFlow] of transformed values.
 */
inline fun <T, R> Flow<T>.mapCached(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    dispatcher: CoroutineContext = Dispatchers.Unconfined,
    replay: Int = 1,
    crossinline transform: suspend (value: T, previous: R?) -> R
): SharedFlow<R> = flow {
    var latest: R? = null
    this@mapCached.map {
        val current = transform.invoke(it, latest)
        emit(current)
        latest = current
    }.collect()
}.flowOn(dispatcher).shareIn(scope, started, replay)

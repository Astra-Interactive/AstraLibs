package ru.astrainteractive.astralibs.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

interface ReloadableFlow<T> : Flow<T> {
    fun reload()
}

/**
 * Reloadable flow is used for
 * @param [flow] original flow with replay
 */
internal class ReloadableFlowImpl<T>(
    flow: Flow<T>,
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    replay: Int = 1
) : ReloadableFlow<T> {
    private val reloadEvent = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        replay = 1
    )
    private val sharedFlow = reloadEvent
        .map { flow.first() }
        .shareIn(scope, started, replay)

    override suspend fun collect(collector: FlowCollector<T>) {
        sharedFlow.collect(collector)
    }

    /**
     * Reload current flow
     */
    override fun reload() {
        reloadEvent.tryEmit(Unit)
    }

    init {
        reloadEvent.tryEmit(Unit)
    }
}

interface ReloadableStateFlow<T> : ReloadableFlow<T>, StateFlow<T>

internal class ReloadableStateFlowImpl<T>(
    private val reloadableFlow: ReloadableFlow<T>,
    scope: CoroutineScope,
    initial: T,
    started: SharingStarted = SharingStarted.Eagerly,
) : ReloadableStateFlow<T> {
    private val stateFlow = reloadableFlow.stateIn(scope, started, initial)

    override val replayCache: List<T>
        get() = stateFlow.replayCache

    override val value: T
        get() = stateFlow.value

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        stateFlow.collect(collector)
    }

    override fun reload() = reloadableFlow.reload()
}

fun <T> Flow<T>.shareInReloadable(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    replay: Int = 1
): ReloadableFlow<T> {
    if (this is ReloadableFlow || this is SharedFlow) {
        error("Using ReloadableFlow instead of flow will lead to unexpected behaviour")
    }
    return ReloadableFlowImpl(
        flow = this,
        scope = scope,
        started = started,
        replay = replay
    )
}

fun <T> Flow<T>.stateInReloadable(
    scope: CoroutineScope,
    initial: T,
    started: SharingStarted = SharingStarted.Eagerly,
    replay: Int = 1
): ReloadableStateFlow<T> {
    if (this is ReloadableFlow || this is SharedFlow) {
        error("Using ReloadableFlow instead of flow will lead to unexpected behaviour")
    }
    return ReloadableStateFlowImpl(
        reloadableFlow = shareInReloadable(
            scope = scope,
            started = started,
            replay = replay
        ),
        scope = scope,
        initial = initial,
        started = started,
    )
}

fun <T> reloadableFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    replay: Int = 1,
    block: suspend FlowCollector<T>.() -> Unit
): ReloadableFlow<T> = flow(block).shareInReloadable(
    scope = scope,
    started = started,
    replay = replay
)

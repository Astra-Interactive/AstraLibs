package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import ru.astrainteractive.astralibs.lifecycle.Lifecycle

/**
 * Long-lived, restartable component driven by the [Lifecycle] contract.
 *
 * Implementations must honour the following contract:
 * - [onEnable] is idempotent: calling it while a run is already in flight does nothing.
 * - [onDisable] only requests a stop; it never throws and is safe before the first [onEnable].
 * - [onReload] restarts the service and leaves it running, whatever state it was in.
 * - A service is reusable: [onEnable] after [onDisable] starts it again.
 * - [state] turns [State.Idle] only once the work has finished unwinding, and stays
 *   [State.Active] during that window, so a stop can be awaited with [awaitIdle].
 */
interface Service : Lifecycle {
    val state: StateFlow<State>

    override fun onEnable()

    override fun onDisable()

    override fun onReload() {
        onDisable()
        onEnable()
    }

    enum class State {
        /** Not running: either never started, or stopped and fully unwound. */
        Idle,
        Active
    }
}

/**
 * Suspends until this service has fully stopped.
 *
 * [Service.onDisable] only requests a stop, so a graceful shutdown is the two combined:
 * `service.onDisable(); service.awaitIdle()`.
 */
suspend fun Service.awaitIdle() {
    state.first { serviceState -> serviceState == Service.State.Idle }
}

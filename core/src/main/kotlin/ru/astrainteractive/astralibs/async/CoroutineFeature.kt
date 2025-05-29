package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * A base interface representing a coroutine-enabled feature with a defined [CoroutineScope].
 * This allows implementing classes to launch coroutines with a specific [CoroutineContext].
 */
interface CoroutineFeature : CoroutineScope {
    /**
     * The coroutine context used to launch coroutines.
     * Must be defined by the implementing class.
     */
    override val coroutineContext: CoroutineContext

    /**
     * A [CoroutineFeature] implementation using [Dispatchers.Unconfined].
     *
     * This context starts coroutines in the current call-frame until the first suspension.
     * Suitable for lightweight operations or testing.
     */
    class Unconfined : CoroutineFeature {
        /**
         * Uses [Dispatchers.Unconfined], a [SupervisorJob] to prevent cancellation propagation,
         * and default coroutine timing configuration.
         */
        override val coroutineContext = SupervisorJob()
            .plus(Dispatchers.Unconfined)
            .plus(CoroutineTimings.Default())
    }

    /**
     * A configurable [CoroutineFeature] implementation that allows specifying a custom [CoroutineContext].
     *
     * @property coroutineContext The coroutine context used for launching coroutines.
     */
    class Default(override val coroutineContext: CoroutineContext) : CoroutineFeature {
        /**
         * Secondary constructor allowing to specify only a [CoroutineDispatcher].
         * The context will include a [SupervisorJob] and default coroutine timing configuration.
         *
         * @param coroutineDispatcher Dispatcher to be used for coroutine execution.
         */
        constructor(coroutineDispatcher: CoroutineDispatcher) : this(
            coroutineContext = SupervisorJob()
                .plus(coroutineDispatcher)
                .plus(CoroutineTimings.Default())
        )
    }
}

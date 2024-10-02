package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

interface CoroutineFeature : CoroutineScope {
    override val coroutineContext: CoroutineContext

    class Unconfined : CoroutineFeature {
        override val coroutineContext = SupervisorJob()
            .plus(Dispatchers.Unconfined)
            .plus(CoroutineTimings.Default())
    }

    class Default(override val coroutineContext: CoroutineContext) : CoroutineFeature {
        constructor(coroutineDispatcher: CoroutineDispatcher) : this(
            coroutineContext = SupervisorJob()
                .plus(coroutineDispatcher)
                .plus(CoroutineTimings.Default())
        )
    }
}

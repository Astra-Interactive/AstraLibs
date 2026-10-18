package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.astrainteractive.astralibs.service.model.OverflowPolicy
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import kotlin.time.Duration

public interface IntervalService : Service

/**
 * [Service] that runs [task] repeatedly, once per [interval].
 *
 * - A new [interval] value is applied immediately and starts a fresh tick; repeated equal values
 *   are ignored, and non-positive ones are skipped with a warning instead of spinning. Under
 *   [OverflowPolicy.Await] that restart also cancels the run in flight.
 * - [getInitialDelay] is queried on every start, including after [Service.onReload], so a
 *   reloaded config takes effect without recreating the service.
 * - A failing [task] is reported through [logger] and never stops the service.
 * - A [task] that outlives [timeout] is cancelled, reported, and the schedule continues.
 * - [scope] is caller-owned: the service launches a child job in it and cancels only that child,
 *   never [scope] itself.
 * - [task] runs on [scope]'s dispatcher, so it must not touch thread-confined server APIs unless
 *   that dispatcher is the main one.
 *
 * Only [timeout] is validated eagerly. Values that arrive at runtime, through [interval] or
 * [getInitialDelay], are skipped or coerced with a warning, and a [getInitialDelay] that throws is
 * reported, so a bad config can never kill the service.
 */
public fun IntervalService(
    interval: Flow<Duration>,
    scope: CoroutineScope,
    logger: Logger,
    task: ServiceTask,
    getInitialDelay: () -> Duration = { Duration.ZERO },
    overflowPolicy: OverflowPolicy = OverflowPolicy.Await,
    timeout: Duration = Duration.INFINITE,
): IntervalService = CoroutineIntervalService(
    interval = interval,
    scope = scope,
    logger = logger,
    task = task,
    getInitialDelay = getInitialDelay,
    overflowPolicy = overflowPolicy,
    timeout = timeout
)

public fun IntervalService(
    interval: Duration,
    scope: CoroutineScope,
    logger: Logger,
    task: ServiceTask
): IntervalService = IntervalService(
    interval = flowOf(interval),
    scope = scope,
    logger = logger,
    task = task
)

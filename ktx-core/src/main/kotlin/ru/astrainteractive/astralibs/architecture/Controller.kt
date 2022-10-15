package ru.astrainteractive.astralibs.architecture

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class Controller : AsyncComponent() {
    val controllerScope: CoroutineScope
        get() = scope

}
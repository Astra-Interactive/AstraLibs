package ru.astrainteractive.astralibs.architecture

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class ViewModel : AsyncComponent() {
    val viewModelScope: CoroutineScope
        get() = scope

}
package ru.astrainteractive.astralibs.architecture

import kotlinx.coroutines.CoroutineScope
import ru.astrainteractive.astralibs.async.AsyncComponent


abstract class Presenter<T : IBaseView>() : AsyncComponent() {
    abstract val viewState: T
    val presenterScope: CoroutineScope
        get() = componentScope

    abstract fun onBinded()

}
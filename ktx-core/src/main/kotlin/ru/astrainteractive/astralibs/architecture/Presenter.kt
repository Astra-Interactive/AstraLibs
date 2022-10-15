package ru.astrainteractive.astralibs.architecture

import kotlinx.coroutines.CoroutineScope


abstract class Presenter<T : IBaseView>() : AsyncComponent() {
    abstract val viewState: T
    val presenterScope: CoroutineScope
        get() = scope

    abstract fun onBinded()

}
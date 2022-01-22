package com.astrainteractive.astralibs.observer


/**
 * LiveData which allows you to subscribe and take up-to-date values
 *
 * Almost like in Android Studio
 */
abstract class LiveData<T> {
    protected constructor(value: T?) {
        this.value = value
    }

    protected constructor() {
        this.value = null
    }

    open var value: T?
        protected set(value) {
            field = value
            listeners.forEach { observers ->
                observers.value.forEach { it.onChanged(value) }
            }
        }
    private val _listeners = mutableMapOf<LifecycleOwner, MutableList<Observer<T?>>>()

    private val listeners
        get() = synchronized(this) {
            _listeners.toMap()
        }

    private fun addListener(observer: LifecycleOwner, observable: Observer<T?>) = synchronized(this) {
        if (!_listeners.containsKey(observer))
            _listeners[observer] = mutableListOf()
        _listeners[observer]?.add(observable)
    }

    private fun removeListener(owner: LifecycleOwner) = synchronized(this) {
        _listeners.remove(owner)
    }


    open fun observe(owner: LifecycleOwner, observer: Observer<T?>) {
        addListener(owner, observer)
    }

    fun removeObserver(observer: LifecycleOwner) {
        removeListener(observer)
    }


}


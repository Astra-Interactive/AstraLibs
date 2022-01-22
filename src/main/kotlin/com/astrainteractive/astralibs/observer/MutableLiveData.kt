package com.astrainteractive.astralibs.observer

/**
 * Observer of object [T]
 *
 * You can define object which you need to listen to update it in gui or anywhere you want
 */
class MutableLiveData<T> : LiveData<T> {


    constructor(value: T) : super(value)


    constructor() : super()

    override var value: T?
        get() = super.value
        public set(value) {
            super.value = value
        }
}


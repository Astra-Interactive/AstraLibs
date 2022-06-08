package com.astrainteractive.astralibs.observer

/**
 * If you want to use [LiveData] - you need to declare [LifecycleOwner] in your class
 *
 * Don't forget to unsubscribe observers to avoid memory leaks!
 */
interface LifecycleOwner{
    fun <T> LiveData<T>.removeObserver(){
        this.removeObserver(this@LifecycleOwner)
    }

}
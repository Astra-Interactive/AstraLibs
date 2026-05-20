package ru.astrainteractive.astralibs.service

/** Long-lived component with an explicit [onCreate]/[onDestroy] lifecycle. */
interface Service {
    fun onCreate()
    fun onDestroy()
}

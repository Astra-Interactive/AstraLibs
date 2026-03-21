package ru.astrainteractive.astralibs.server

fun interface KCommandDispatcher {
    fun dispatchCommand(command: String)
}

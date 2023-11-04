package ru.astrainteractive.astralibs.command.api

/**
 * Executor will execute command depending on it's input
 */
fun interface CommandExecutor<I : Any> {
    fun execute(input: I)
}

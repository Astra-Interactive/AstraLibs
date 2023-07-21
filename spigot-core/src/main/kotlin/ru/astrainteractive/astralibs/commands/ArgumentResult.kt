package ru.astrainteractive.astralibs.commands

sealed class ArgumentResult<T>(val rawValue: String?) {

    class Success<T>(val value: T, rawValue: String?) : ArgumentResult<T>(rawValue)

    class Failure(rawValue: String?) : ArgumentResult<Nothing>(rawValue)

    inline fun onSuccess(action: (Success<T>) -> Unit): ArgumentResult<T> {
        successOrNull()?.let(action)
        return this
    }

    inline fun onResult(action: (T) -> Unit): ArgumentResult<T> {
        successOrNull()?.value?.let(action)
        return this
    }

    inline fun onFailure(action: (Failure) -> Unit): ArgumentResult<T> {
        failureOrNull()?.let(action)
        return this
    }

    /**
     * Returns success result of argument
     */
    fun successOrNull(): Success<T>? = this as? Success<T>

    /**
     * Returns result of parsed argument
     */
    fun resultOrNull(): T? = successOrNull()?.value

    /**
     * Returns failure result of argument
     */
    fun failureOrNull(): Failure? = this as? Failure
}

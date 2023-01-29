package ru.astrainteractive.astralibs.commands

sealed class Argument<T>(val rawValue: String?) {
    inline fun onSuccess(action: (Argument.Success<T>) -> Unit): Argument<T> {
        (this as? Argument.Success<T>)?.let(action)
        return this
    }
    inline fun onFailure(action: (Argument.Failure<T>) -> Unit): Argument<T> {
        (this as? Argument.Failure<T>)?.let(action)
        return this
    }
    fun successOrNull(): Argument.Success<T>? = this as? Argument.Success<T>
    fun failureOrNull(): Argument.Failure<T>? = this as? Argument.Failure<T>

    class Success<T>(val value: T, rawValue: String?) : Argument<T>(rawValue)
    class Failure<T>(rawValue: String?) : Argument<T>(rawValue)
}
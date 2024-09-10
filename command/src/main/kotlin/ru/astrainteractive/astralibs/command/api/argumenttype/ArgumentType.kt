package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.DefaultCommandException
import kotlin.jvm.Throws

interface ArgumentType<T : Any> {
    val key: String

    @Throws(DefaultCommandException.ArgumentTypeException::class)
    fun transform(value: String): T

    class Lambda<T : Any>(
        override val key: String,
        private val transform: (String) -> T
    ) : ArgumentType<T> {
        override fun transform(value: String): T = transform.invoke(value)
    }
}

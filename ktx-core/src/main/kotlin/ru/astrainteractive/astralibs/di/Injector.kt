package ru.astrainteractive.astralibs.di


/**
 * Very simple DI for your plugin
 */
object Injector {
    /**
     * This is a list of static objects
     */
    private val _objects = mutableSetOf<Any?>()

    /**
     * Need a public reference to use reified and inline
     */
    val objects: Set<Any?>
        get() = _objects

    /**
     * Will return value of type [T]
     */
    inline fun <reified T> inject(): T = objects.firstOrNull { it is T } as T

    /**
     * Will return value of type [T] lazily
     */
    inline fun <reified T : Any> lazyInject() = lazy { inject<T>() }

    /**
     * Will remember object
     */
    fun <T> remember(obj: T) = _objects.add(obj)

    /**
     * Will forget object
     */
    fun <T : Any> forget(obj: T) = _objects.remove(obj)

    /**
     * Will forget everything
     */
    fun forgetAll() = _objects.clear()
}
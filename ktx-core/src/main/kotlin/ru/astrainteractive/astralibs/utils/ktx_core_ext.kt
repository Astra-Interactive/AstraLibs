package ru.astrainteractive.astralibs.utils

import java.sql.ResultSet
import kotlin.random.Random


/**
 * Catching errors
 * @return null if Exception happened or result of [block]
 */
inline fun <T> catching(stackTrace: Boolean = false, onError: (Exception) -> Unit = {}, block: () -> T?): T? {
    return try {
        val result = block()
        result
    } catch (e: Exception) {
        onError(e)
        if (stackTrace)
            e.printStackTrace()
        // TODO
//        Logger.log(e.stackTraceToString(),"Catching",false)
        null
    }
}

/**
 * Safely get value from enum with type [T]
 * @return T or null if value in [Enum] not found
 */
inline fun <reified T : Enum<T>> valueOfOrNull(type: String): T? =
    catching {
        java.lang.Enum.valueOf(T::class.java, type)
    }


/**
 * For loop for ResultSet
 */
inline fun ResultSet.forEach(rs: (ResultSet) -> Unit) {
    while (this.next()) {
        rs(this)
    }
}

/**
 * Map not null for result set
 */
public inline fun <R : Any> ResultSet.mapNotNull(rs: (ResultSet) -> R?): List<R> {
    return mapNotNullTo(ArrayList<R>(), rs)
}

/**
 * Map not null for result set
 */
public inline fun <R : Any, C : MutableCollection<in R>> ResultSet.mapNotNullTo(
    destination: C,
    rs: (ResultSet) -> R?,
): C {
    forEach { element -> rs(element)?.let { destination.add(it) } }
    return destination
}

inline fun <reified T : kotlin.Enum<T>> T.addIndex(offset: Int): T {
    val values = T::class.java.enumConstants
    var res = ordinal + offset
    if (res <= -1) res = values.size - 1
    val index = res % values.size
    return values[index]
}

inline fun <reified T : kotlin.Enum<T>> T.next(): T {
    return addIndex(1)
}

inline fun <reified T : kotlin.Enum<T>> T.prev(): T {
    return addIndex(-1)
}

val <T> List<T>.randomElementOrNull: T?
    get() {
        if (isEmpty()) return null
        return this.getOrNull(Random.nextInt(size))
    }

infix fun <T> Boolean.then(param: T): T? = if (this) param else null
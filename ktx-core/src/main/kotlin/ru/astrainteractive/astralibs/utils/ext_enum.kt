package ru.astrainteractive.astralibs.utils
/**
 * Safely get value from enum with type [T]
 * @return T or null if value in [Enum] not found
 */
inline fun <reified T : Enum<T>> getValueOf(type: String): Result<T> = runCatching {
    java.lang.Enum.valueOf(T::class.java, type)
}

inline fun <reified T : kotlin.Enum<T>> T.addIndex(offset: Int): T {
    val values = T::class.java.enumConstants
    var res = ordinal + offset
    if (res <= -1) res = values.size - 1
    val index = res % values.size
    return values[index]
}

/**
 * Returns next indexed enum
 */
inline fun <reified T : kotlin.Enum<T>> T.next(): T {
    return addIndex(1)
}

/**
 * Returns previously indexed enum
 */
inline fun <reified T : kotlin.Enum<T>> T.prev(): T {
    return addIndex(-1)
}
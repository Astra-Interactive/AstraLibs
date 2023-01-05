package ru.astrainteractive.astralibs.utils

import java.sql.ResultSet


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



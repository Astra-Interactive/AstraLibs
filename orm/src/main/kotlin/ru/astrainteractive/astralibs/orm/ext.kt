package ru.astrainteractive.astralibs.orm

import java.sql.ResultSet
import java.sql.Statement

internal fun <T> T.sqliteString(): T = if (this is String) "\"$this\"" as T else this

/**
 * Statement will be closed after evaluation of [block]
 */
fun <T> Statement.with(block: Statement.()->T): T{
    val value = block()
    this.close()
    return value
}

/**
 * Returns first entry of result set or null if not found
 */
fun <T> ResultSet.firstOrNull(block:(ResultSet)->T):T?{
    return if (this.next()){
        block(this)
    } else null
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


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
fun <T> ResultSet.firstOrNull(block:(ResultSet)->T):T?{
    return if (this.next()){
        block(this)
    } else null
}
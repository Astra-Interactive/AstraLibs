package ru.astrainteractive.astralibs.orm.statement

import ru.astrainteractive.astralibs.orm.database.Column

class DBStatement {
    private val _values: MutableMap<Column<*>, Any?> = LinkedHashMap()
    val values: Map<Column<*>, Any?>
        get() = _values

    operator fun <T> set(column: Column<T>, value: T?) {
        _values[column] = value
    }
}

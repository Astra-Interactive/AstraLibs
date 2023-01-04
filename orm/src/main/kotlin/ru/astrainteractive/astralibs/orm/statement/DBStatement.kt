package ru.astrainteractive.astralibs.orm.statement

import ru.astrainteractive.astralibs.orm.database.Column

class DBStatement {
    val values: MutableMap<Column<*>, Any?> = LinkedHashMap()
    operator fun <T> set(column: Column<T>, value: T?) {
        values[column] = value
    }
}
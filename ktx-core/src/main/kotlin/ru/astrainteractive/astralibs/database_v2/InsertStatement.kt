package ru.astrainteractive.astralibs.database_v2

class InsertStatement(val table: Table<*>) {
    val values: MutableMap<Column<*>, Any?> = LinkedHashMap()

    operator fun <T> set(column: Column<T>, value: T) {
        values[column] = value
    }
}
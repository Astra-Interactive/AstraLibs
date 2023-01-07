package ru.astrainteractive.astralibs.orm.database

import kotlin.reflect.KProperty

abstract class Entity<T>(val table: Table<T>) {
    private val writeValues = LinkedHashMap<Column<Any?>, Any?>()

    operator fun <K> Column<K>.getValue(o: Entity<T>, desc: KProperty<*>): K {
        return writeValues[this as Column<Any?>] as K
    }

    operator fun <K> Column<K>.setValue(o: Entity<T>, desc: KProperty<*>, value: K) {
        if (!table.columns.contains(this)) throw IllegalStateException("Table ${table.tableName} does not contains column named ${this.name}")
        writeValues[this as Column<Any?>] = value
    }

    operator fun set(column: Column<Any?>, value: Any?) {
        if (!table.columns.contains(column)) throw IllegalStateException("Table ${table.tableName} does not contains column named ${column.name}")
        writeValues[column] = value
    }
    operator fun <K> get(column: Column<K>): K {
        return writeValues[column as Column<Any?>] as K
    }
}
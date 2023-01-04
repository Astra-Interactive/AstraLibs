package ru.astrainteractive.astralibs.orm.database


sealed class Column<T>(val name: String, val type: String) {
    var primaryKey: Boolean = false
        private set
    var autoIncrement: Boolean = false
        private set
    var nullable: Boolean = false
        private set
    var unique: Boolean = false
        private set

    fun unique(): Column<T> = apply {
        unique = true
    }

    fun nullable(): Column<T> = apply {
        nullable = true
    }

    fun autoIncrement(): Column<T> = apply {
        autoIncrement = true
    }

    fun primaryKey(): Column<T> = apply {
        primaryKey = true
    }
    class IntColumn(name: String) : Column<Int>(name, "INTEGER")
    class LongColumn(name: String) : Column<Long>(name, "INTEGER")
    class StringColumn(name: String) : Column<String>(name, "TEXT")
    class BoolColumn(name: String) : Column<Int>(name, "BOOLEAN")
    class DoubleColumn(name: String) : Column<Double>(name, "REAL")
    class FloatColumn(name: String) : Column<Float>(name, "REAL")
    class ByteArrayColumn(name: String) : Column<ByteArray>(name, "VARBINARY")
}


package ru.astrainteractive.astralibs.database_v2


class Column<T>(val name: String, val type: String) {
    var primaryKey: Boolean = false
        private set
    var autoIncrement: Boolean = false
        private set
    var nullable: Boolean = false
        private set
    var unique: Boolean = false
        private set

    fun unique(): Column<T> {
        this.unique = true
        return this
    }

    fun nullable(): Column<T> {
        this.nullable = true
        return this
    }

    fun autoIncrement(): Column<T> {
        autoIncrement = true
        return this
    }

    fun primaryKey(): Column<T> {
        primaryKey = true
        return this
    }
}


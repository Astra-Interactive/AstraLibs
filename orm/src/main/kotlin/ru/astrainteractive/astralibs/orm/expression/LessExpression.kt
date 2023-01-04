package ru.astrainteractive.astralibs.orm.expression

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.sqliteString

class LessExpression<T>(val column: Column<T>, val value: T) : Expression<Boolean> {
    override fun toString(): String {
        return "${column.name} < ${value.sqliteString()}"
    }
}
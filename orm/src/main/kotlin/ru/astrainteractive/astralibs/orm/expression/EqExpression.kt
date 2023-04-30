package ru.astrainteractive.astralibs.orm.expression

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.sqliteString

sealed interface EqExpression<T> : Expression<Boolean> {
    val column: Column<T>

    class Value<T>(override val column: Column<T>, val value: T) : EqExpression<T> {
        override fun toString(): String {
            return "${column.name} = ${value.sqliteString()}"
        }
    }

    class Query<T>(
        override val column: Column<T>,
        val query: ru.astrainteractive.astralibs.orm.query.Query
    ) : EqExpression<T> {
        override fun toString(): String {
            return "${column.name} = (${query.generate()})"
        }
    }
}

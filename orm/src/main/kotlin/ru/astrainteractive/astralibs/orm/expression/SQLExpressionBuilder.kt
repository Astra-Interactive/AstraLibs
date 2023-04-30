package ru.astrainteractive.astralibs.orm.expression

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.query.Query

object SQLExpressionBuilder {
    infix fun <T> Column<T>.eq(value: T): Expression<Boolean> {
        return EqExpression.Value(this, value)
    }
    infix fun Column<*>.eq(query: () -> Query): Expression<Boolean> {
        return EqExpression.Query(this, query())
    }
    infix fun <T> Column<T>.notEq(value: T): Expression<Boolean> {
        return NotEqExpression(this, value)
    }
    infix fun <T> Column<T>.more(value: T): Expression<Boolean> {
        return MoreExpression(this, value)
    }
    infix fun <T> Column<T>.less(value: T): Expression<Boolean> {
        return LessExpression(this, value)
    }

    fun resolveExpression(ep: Expression<*>): String {
        return ep.toString()
    }
}

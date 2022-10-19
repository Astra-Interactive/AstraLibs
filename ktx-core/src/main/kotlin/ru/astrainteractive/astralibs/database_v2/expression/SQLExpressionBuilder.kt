package ru.astrainteractive.astralibs.database_v2.expression

import ru.astrainteractive.astralibs.database_v2.Column

object SQLExpressionBuilder {
    infix fun <T> Column<T>.eq(value: T): Expression<Boolean> {
        return EqExpression(this, value)
    }
    fun resolveExpression(ep: Expression<*>): String {
        return ep.toString()
    }

}
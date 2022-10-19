package ru.astrainteractive.astralibs.database_v2.expression

class AndExpression<T>(val parent: Expression<*>, val child: Expression<*>) : Expression<T> {
    override fun toString(): String {
        return SQLExpressionBuilder.resolveExpression(child) + " AND (" + SQLExpressionBuilder.resolveExpression(
            parent
        ) + ")"
    }
}
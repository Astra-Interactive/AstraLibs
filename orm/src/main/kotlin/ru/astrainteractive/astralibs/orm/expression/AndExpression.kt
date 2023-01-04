package ru.astrainteractive.astralibs.orm.expression

class AndExpression<T>(val parent: Expression<*>, val child: Expression<*>) : Expression<T> {
    override fun toString(): String {
        val childExpression = SQLExpressionBuilder.resolveExpression(child)
        val parentExpression = SQLExpressionBuilder.resolveExpression(parent)
        return "$childExpression AND ($parentExpression)"
    }
}
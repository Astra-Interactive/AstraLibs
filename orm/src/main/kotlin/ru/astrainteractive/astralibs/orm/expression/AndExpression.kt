package ru.astrainteractive.astralibs.orm.expression

import ru.astrainteractive.astralibs.orm.query.Query

sealed interface AndExpression<T> : Expression<T> {
    val realParent: Expression<*>
    class ChildExpression<T>(
        val realChild: Expression<*>,
        override val realParent: Expression<*>
    ) : AndExpression<T> {
        override fun toString(): String {
            val realParentExpression = SQLExpressionBuilder.resolveExpression(realParent)
            val realChildExpression = SQLExpressionBuilder.resolveExpression(realChild)
            return "$realParentExpression AND ($realChildExpression)"
        }
    }
    class ChildQuery<T>(
        val realChild: Query,
        override val realParent: Expression<*>
    ) : AndExpression<T> {
        override fun toString(): String {
            val realParentExpression = SQLExpressionBuilder.resolveExpression(realParent)
            return "$realParentExpression AND (${realChild.generate()})"
        }
    }
}

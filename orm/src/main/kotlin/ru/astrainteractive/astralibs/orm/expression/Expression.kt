package ru.astrainteractive.astralibs.orm.expression

import ru.astrainteractive.astralibs.orm.query.Query

sealed interface Expression<T> {
    override fun toString(): String

    fun and(eq: Expression<T>): Expression<T> {
        return AndExpression.ChildExpression(eq, this)
    }
    fun and(eq: () -> Expression<T>): Expression<T> {
        return AndExpression.ChildExpression(eq(), this)
    }
    fun andQuery(eq: () -> Query): Expression<T> {
        return AndExpression.ChildQuery<T>(eq(), this)
    }

    object Empty : Expression<Boolean> {
        override fun toString(): String = ""
    }
}

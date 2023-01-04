package ru.astrainteractive.astralibs.orm.expression

sealed interface Expression<T> {
    override fun toString(): String

    fun and(eq: Expression<T>): Expression<T> {
        return AndExpression(eq, this)
    }

    object Empty : Expression<Boolean> {
        override fun toString(): String = ""
    }
}


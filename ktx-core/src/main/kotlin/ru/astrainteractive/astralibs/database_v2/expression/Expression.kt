package ru.astrainteractive.astralibs.database_v2.expression

sealed interface Expression<T> {
    override fun toString(): String

    fun and(eq: Expression<T>): Expression<T> {
        return AndExpression(eq, this)
    }

    companion object {
        inline fun <T, E : Expression<T>> build(builder: SQLExpressionBuilder.() -> E): E =
            SQLExpressionBuilder.builder()
    }
}


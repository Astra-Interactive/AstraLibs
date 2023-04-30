package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Table
import ru.astrainteractive.astralibs.orm.expression.Expression
import ru.astrainteractive.astralibs.orm.expression.SQLExpressionBuilder

class CountQuery(
    val table: Table<*>,
    val expression: SQLExpressionBuilder.() -> Expression<Boolean> = { Expression.Empty }
) : Query {
    override fun generate(): String {
        val op: Expression<Boolean> = SQLExpressionBuilder.expression()
        var query = "SELECT COUNT(*) AS total FROM ${table.tableName}"
        if (op.toString().isNotEmpty()) {
            query = "$query WHERE $op"
        }
        return query
    }
}

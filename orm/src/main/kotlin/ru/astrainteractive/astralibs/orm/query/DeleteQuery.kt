package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Table
import ru.astrainteractive.astralibs.orm.expression.Expression
import ru.astrainteractive.astralibs.orm.expression.SQLExpressionBuilder

class DeleteQuery(
    val table: Table<*>,
    val expression: SQLExpressionBuilder.() -> Expression<Boolean>
) : Query {
    override fun generate(): String {
        val op: Expression<Boolean> = SQLExpressionBuilder.expression()
        val query = "DELETE FROM ${table.tableName} WHERE $op"
        return query
    }
}

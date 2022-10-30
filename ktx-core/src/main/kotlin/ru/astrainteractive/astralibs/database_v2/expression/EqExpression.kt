package ru.astrainteractive.astralibs.database_v2.expression

import ru.astrainteractive.astralibs.database_v2.Column
import ru.astrainteractive.astralibs.database_v2.fixIfString

class EqExpression<T>(val column: Column<T>, val value: T) : Expression<Boolean> {
    override fun toString(): String {
        return "${column.name} = ${value.fixIfString()}"
    }
}

class MoreExpression<T>(val column: Column<T>, val value: T) : Expression<Boolean> {
    override fun toString(): String {
        return "${column.name} > ${value.fixIfString()}"
    }
}

class LessExpression<T>(val column: Column<T>, val value: T) : Expression<Boolean> {
    override fun toString(): String {
        return "${column.name} < ${value.fixIfString()}"
    }
}

class NotEqExpression<T>(val column: Column<T>, val value: T) : Expression<Boolean> {
    override fun toString(): String {
        return "${column.name} != ${value.fixIfString()}"
    }
}
package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.DefaultCommandException.ArgumentTypeException

interface PrimitiveArgumentType<T : Any> : ArgumentType<T> {
    data object Int : PrimitiveArgumentType<kotlin.Int> {
        override val key: kotlin.String = "INT"
        override fun transform(value: kotlin.String): kotlin.Int {
            return value.toIntOrNull() ?: throw ArgumentTypeException(Double.key, value)
        }
    }

    data object String : PrimitiveArgumentType<kotlin.String> {
        override val key: kotlin.String = "STRING"
        override fun transform(value: kotlin.String): kotlin.String {
            return value
        }
    }

    data object Double : PrimitiveArgumentType<kotlin.Double> {
        override val key: kotlin.String = "DOUBLE"
        override fun transform(value: kotlin.String): kotlin.Double {
            return value.toDoubleOrNull() ?: throw ArgumentTypeException(key, value)
        }
    }

    data object Boolean : PrimitiveArgumentType<kotlin.Boolean> {
        override val key: kotlin.String = "BOOLEAN"
        override fun transform(value: kotlin.String): kotlin.Boolean {
            return value.toBooleanStrictOrNull() ?: throw ArgumentTypeException(Double.key, value)
        }
    }
}

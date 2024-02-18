package ru.astrainteractive.astralibs.command.api.argumenttype

interface PrimitiveArgumentType<T> : ArgumentType<T> {
    data object Int : PrimitiveArgumentType<kotlin.Int?> {
        override fun transform(value: kotlin.String?): kotlin.Int? {
            return value?.toIntOrNull()
        }
    }

    data object String : PrimitiveArgumentType<kotlin.String?> {
        override fun transform(value: kotlin.String?): kotlin.String? {
            return value
        }
    }

    data object Double : PrimitiveArgumentType<kotlin.Double?> {
        override fun transform(value: kotlin.String?): kotlin.Double? {
            return value?.toDouble()
        }
    }

    data object Boolean : PrimitiveArgumentType<kotlin.Boolean?> {
        override fun transform(value: kotlin.String?): kotlin.Boolean? {
            return value?.toBooleanStrict()
        }
    }
}

package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentTypeException

interface PrimitiveArgumentType<T : Any> : ArgumentType<T>

data object IntArgumentType : PrimitiveArgumentType<Int> {
    override val key: String = "INT"
    override fun transform(value: String): Int {
        return value.toIntOrNull() ?: throw ArgumentTypeException(key, value)
    }
}

data object StringArgumentType : PrimitiveArgumentType<String> {
    override val key: String = "STRING"
    override fun transform(value: String): String {
        return value
    }
}

data object DoubleArgumentType : PrimitiveArgumentType<Double> {
    override val key: String = "DOUBLE"
    override fun transform(value: String): Double {
        return value.toDoubleOrNull() ?: throw ArgumentTypeException(key, value)
    }
}

data object BooleanArgumentType : PrimitiveArgumentType<Boolean> {
    override val key: String = "BOOLEAN"
    override fun transform(value: String): Boolean {
        return value.toBooleanStrictOrNull() ?: throw ArgumentTypeException(key, value)
    }
}

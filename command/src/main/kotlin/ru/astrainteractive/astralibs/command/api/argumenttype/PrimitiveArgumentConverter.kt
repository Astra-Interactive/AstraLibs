package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException

interface PrimitiveArgumentConverter<T : Any> : ArgumentConverter<T>

data object IntArgumentConverter : PrimitiveArgumentConverter<Int> {
    override fun transform(argument: String): Int {
        return argument.toIntOrNull()
            ?: throw ArgumentConverterException(
                clazz = IntArgumentConverter::class.java,
                value = argument
            )
    }
}

data object StringArgumentConverter : PrimitiveArgumentConverter<String> {
    override fun transform(argument: String): String {
        return argument
    }
}

data object DoubleArgumentConverter : PrimitiveArgumentConverter<Double> {
    override fun transform(argument: String): Double {
        return argument.toDoubleOrNull() ?: throw ArgumentConverterException(
            clazz = DoubleArgumentConverter::class.java,
            value = argument
        )
    }
}

data object FloatArgumentConverter : PrimitiveArgumentConverter<Float> {
    override fun transform(argument: String): Float {
        return argument.toFloatOrNull() ?: throw ArgumentConverterException(
            clazz = FloatArgumentConverter::class.java,
            value = argument
        )
    }
}

data object BooleanArgumentConverter : PrimitiveArgumentConverter<Boolean> {
    override fun transform(argument: String): Boolean {
        return argument.toBooleanStrictOrNull()
            ?: throw ArgumentConverterException(
                clazz = BooleanArgumentConverter::class.java,
                value = argument
            )
    }
}

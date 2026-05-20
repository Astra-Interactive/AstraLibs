package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException

/** Marker interface for [ArgumentConverter] implementations that convert Kotlin primitive types. */
interface PrimitiveArgumentConverter<T : Any> : ArgumentConverter<T>

/** Converts a string argument to [Int]. */
data object IntArgumentConverter : PrimitiveArgumentConverter<Int> {
    override fun transform(argument: String): Int {
        return argument.toIntOrNull()
            ?: throw ArgumentConverterException(
                clazz = IntArgumentConverter::class.java,
                value = argument
            )
    }
}

/** Returns the argument string as-is. Never throws. */
data object StringArgumentConverter : PrimitiveArgumentConverter<String> {
    override fun transform(argument: String): String {
        return argument
    }
}

/** Converts a string argument to [Double]. */
data object DoubleArgumentConverter : PrimitiveArgumentConverter<Double> {
    override fun transform(argument: String): Double {
        return argument.toDoubleOrNull() ?: throw ArgumentConverterException(
            clazz = DoubleArgumentConverter::class.java,
            value = argument
        )
    }
}

/** Converts a string argument to [Float]. */
data object FloatArgumentConverter : PrimitiveArgumentConverter<Float> {
    override fun transform(argument: String): Float {
        return argument.toFloatOrNull() ?: throw ArgumentConverterException(
            clazz = FloatArgumentConverter::class.java,
            value = argument
        )
    }
}

/** Converts a string argument to [Boolean]; accepts only `"true"` or `"false"` (strict). */
data object BooleanArgumentConverter : PrimitiveArgumentConverter<Boolean> {
    override fun transform(argument: String): Boolean {
        return argument.toBooleanStrictOrNull()
            ?: throw ArgumentConverterException(
                clazz = BooleanArgumentConverter::class.java,
                value = argument
            )
    }
}

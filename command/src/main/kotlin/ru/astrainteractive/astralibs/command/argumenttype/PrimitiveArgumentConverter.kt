package ru.astrainteractive.astralibs.command.argumenttype

import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.model.ArgumentConverterException

/** Marker interface for [ArgumentConverter] implementations that convert Kotlin primitive types. */
public interface PrimitiveArgumentConverter<T : Any> : ArgumentConverter<T>

/** Converts a string argument to [Int]. */
public data object IntArgumentConverter : PrimitiveArgumentConverter<Int> {
    override fun transform(argument: String): Int {
        return argument.toIntOrNull()
            ?: throw ArgumentConverterException(
                clazz = IntArgumentConverter::class.java,
                value = argument
            )
    }
}

/** Returns the argument string as-is. Never throws. */
public data object StringArgumentConverter : PrimitiveArgumentConverter<String> {
    override fun transform(argument: String): String {
        return argument
    }
}

/** Converts a string argument to [Double]. */
public data object DoubleArgumentConverter : PrimitiveArgumentConverter<Double> {
    override fun transform(argument: String): Double {
        return argument.toDoubleOrNull() ?: throw ArgumentConverterException(
            clazz = DoubleArgumentConverter::class.java,
            value = argument
        )
    }
}

/** Converts a string argument to [Float]. */
public data object FloatArgumentConverter : PrimitiveArgumentConverter<Float> {
    override fun transform(argument: String): Float {
        return argument.toFloatOrNull() ?: throw ArgumentConverterException(
            clazz = FloatArgumentConverter::class.java,
            value = argument
        )
    }
}

/** Converts a string argument to [Boolean]; accepts only `"true"` or `"false"` (strict). */
public data object BooleanArgumentConverter : PrimitiveArgumentConverter<Boolean> {
    override fun transform(argument: String): Boolean {
        return argument.toBooleanStrictOrNull()
            ?: throw ArgumentConverterException(
                clazz = BooleanArgumentConverter::class.java,
                value = argument
            )
    }
}

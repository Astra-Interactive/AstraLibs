package ru.astrainteractive.astralibs.util

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> Any.privateField(
    fieldName: String,
    onError: (Throwable) -> T = { t -> error(t) }
): T = try {
    val field = this::class.java.getDeclaredField(fieldName)
    field.isAccessible = true
    field.get(this) as T
} catch (e: Throwable) {
    onError.invoke(e)
}

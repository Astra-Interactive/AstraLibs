package ru.astrainteractive.astralibs.encoding.encoder

import java.io.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class ObjectEncoderTest {
    private val javaObjectEncoder = JavaObjectEncoder()

    private inline fun <reified T> testToFromByteArray(initial: T) {
        val encoded = javaObjectEncoder.toByteArray(initial)
        val decoded: T = javaObjectEncoder.fromByteArray(encoded)
        assertEquals(initial, decoded)
    }

    private inline fun <reified T> testToFromBase64(initial: T) {
        val encoded = javaObjectEncoder.toBase64(initial)
        val decoded: T = javaObjectEncoder.fromBase64(encoded)
        assertEquals(initial, decoded)
    }

    @Suppress("SerialVersionUIDInSerializableClass")
    data class CustomClass(
        val string: String = "Hello word",
        val list: List<String> = listOf("Hello"),
        val innerClass: InnerClass = InnerClass("hello")
    ) : Serializable

    @Suppress("SerialVersionUIDInSerializableClass")
    data class InnerClass(val string: String) : Serializable

    @Test
    fun GIVEN_classes_WHEN_to_from_byte_array_THEN_equals() {
        testToFromByteArray("Hello world")
        testToFromByteArray(10)
        testToFromByteArray(10.0)
        testToFromByteArray(0f)
        testToFromByteArray(false)
        testToFromByteArray('A')
        testToFromByteArray(null as String?)
        testToFromByteArray(listOf(1, 2, 3))
        testToFromByteArray(CustomClass())
    }

    @Test
    fun GIVEN_classes_WHEN_to_from_base64_THEN_equals() {
        testToFromBase64("Hello world")
        testToFromBase64(10)
        testToFromBase64(10.0)
        testToFromBase64(0f)
        testToFromBase64(false)
        testToFromBase64('A')
        testToFromBase64(null as String?)
        testToFromBase64(listOf(1, 2, 3))
        testToFromBase64(CustomClass())
    }
}

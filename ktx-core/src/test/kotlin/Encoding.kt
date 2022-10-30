import ru.astrainteractive.astralibs.utils.encoding.Serializer
import java.io.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class Encoding {
    private inline fun <reified T> toFromByteArray(initial: T) {
        val encoded = Serializer.toByteArray(initial)
        val decoded: T = Serializer.fromByteArray(encoded)
        assertEquals(initial, decoded)
    }
    private inline fun <reified T> toFromBase64(initial: T) {
        val encoded = Serializer.toBase64(initial)
        val decoded: T = Serializer.fromBase64(encoded)
        assertEquals(initial, decoded)
    }

    data class CustomClass(
        val string: String = "Hello word",
        val list: List<String> = listOf("Hello"),
        val innerClass: InnerClass = InnerClass("hello")
    ) : Serializable

    data class InnerClass(val string: String) : Serializable

    @Test
    fun `Test types to and from byteArray equals`() {
        toFromByteArray("Hello world")
        toFromByteArray(10)
        toFromByteArray(10.0)
        toFromByteArray(0f)
        toFromByteArray(false)
        toFromByteArray('A')
        toFromByteArray(null as String?)
        toFromByteArray(listOf(1, 2, 3))
        toFromByteArray(CustomClass())
    }

    @Test
    fun `Test types to and from base64 equals`() {
        toFromBase64("Hello world")
        toFromBase64(10)
        toFromBase64(10.0)
        toFromBase64(0f)
        toFromBase64(false)
        toFromBase64('A')
        toFromBase64(null as String?)
        toFromBase64(listOf(1, 2, 3))
        toFromBase64(CustomClass())
    }
}
package ru.astrainteractive.astralibs.encoding.encoder

import java.io.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName", "SerialVersionUIDInSerializableClass")
class ObjectEncoderExtTest {

    private val encoder: ObjectEncoder = JavaObjectEncoder()

    data class EncoderItem(
        val id: Int,
        val name: String
    ) : Serializable

    @Test
    fun GIVEN_primitive_list_WHEN_encode_decode_THEN_round_trips() {
        val original = listOf(1, 2, 3)

        val encoded = encoder.encodeList(original)
        val decoded = encoder.decodeList<Int>(encoded)

        assertEquals(original, decoded)
    }

    @Test
    fun GIVEN_empty_list_WHEN_encode_decode_THEN_round_trips() {
        val encoded = encoder.encodeList(emptyList<String>())

        assertEquals(emptyList(), encoder.decodeList<String>(encoded))
    }

    @Test
    fun GIVEN_data_class_list_WHEN_encode_decode_THEN_round_trips() {
        val original = listOf(
            EncoderItem(id = 1, name = "first"),
            EncoderItem(id = 2, name = "second")
        )

        val encoded = encoder.encodeList(original)
        val decoded = encoder.decodeList<EncoderItem>(encoded)

        assertEquals(original, decoded)
    }

    @Test
    fun GIVEN_base64_wrapping_null_WHEN_decode_list_THEN_returns_empty_list() {
        val encodedNull = encoder.toBase64<List<Int>?>(null)

        assertEquals(emptyList(), encoder.decodeList<Int>(encodedNull))
    }

    @Test
    fun GIVEN_single_element_list_WHEN_encode_decode_THEN_round_trips() {
        val original = listOf(42)

        assertEquals(original, encoder.decodeList<Int>(encoder.encodeList(original)))
    }

    @Test
    fun GIVEN_list_with_null_elements_WHEN_encode_decode_THEN_round_trips() {
        val original = listOf("a", null, "b")

        assertEquals(original, encoder.decodeList<String?>(encoder.encodeList(original)))
    }

    @Test
    fun GIVEN_nested_list_WHEN_encode_decode_THEN_round_trips() {
        val original = listOf(listOf(1, 2), listOf(3))

        assertEquals(original, encoder.decodeList<List<Int>>(encoder.encodeList(original)))
    }

    @Test
    fun GIVEN_large_list_WHEN_encode_decode_THEN_round_trips() {
        val original = (1..1000).toList()

        assertEquals(original, encoder.decodeList<Int>(encoder.encodeList(original)))
    }
}

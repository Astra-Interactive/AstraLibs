package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.astrainteractive.astralibs.string.StringDesc
import ru.astrainteractive.astralibs.util.YamlStringFormat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class StringDescSerializerTest {

    private val format: StringFormat = YamlStringFormat()

    @Test
    fun GIVEN_raw_desc_WHEN_encode_decode_THEN_round_trips() {
        val value = StringDesc.Raw("&aHello")

        val encoded = format.encodeToString(RawStringDescSerializer, value)
        val decoded = format.decodeFromString(RawStringDescSerializer, encoded)

        assertEquals(value, decoded)
    }

    @Test
    fun GIVEN_plain_desc_WHEN_encode_decode_THEN_round_trips() {
        val value = StringDesc.Plain("Hello")

        val encoded = format.encodeToString(PlainStringDescSerializer, value)
        val decoded = format.decodeFromString(PlainStringDescSerializer, encoded)

        assertEquals(value, decoded)
    }

    @Test
    fun GIVEN_raw_desc_WHEN_default_serializer_round_trips_THEN_equals() {
        val value: StringDesc = StringDesc.Raw("&aHello")

        val encoded = format.encodeToString(DefaultStringDescSerializer, value)
        val decoded = format.decodeFromString(DefaultStringDescSerializer, encoded)

        assertEquals(value, decoded)
    }

    @Test
    fun GIVEN_plain_encoded_string_WHEN_decoded_by_default_serializer_THEN_becomes_raw() {
        val encoded = format.encodeToString(PlainStringDescSerializer, StringDesc.Plain("Hello"))

        val decoded = format.decodeFromString(DefaultStringDescSerializer, encoded)

        assertTrue(decoded is StringDesc.Raw)
        assertEquals("Hello", decoded.raw)
    }

    @Test
    fun GIVEN_sealed_string_desc_WHEN_serialized_via_annotation_default_THEN_decodes_to_raw() {
        val value: StringDesc = StringDesc.Plain("Hello")

        val encoded = format.encodeToString(value)
        val decoded = format.decodeFromString<StringDesc>(encoded)

        assertTrue(decoded is StringDesc.Raw)
        assertEquals("Hello", decoded.raw)
    }

    @Test
    fun GIVEN_empty_string_WHEN_encode_decode_THEN_round_trips() {
        val value = StringDesc.Raw("")

        val encoded = format.encodeToString(RawStringDescSerializer, value)

        assertEquals(value, format.decodeFromString(RawStringDescSerializer, encoded))
    }

    @Test
    fun GIVEN_yaml_special_characters_WHEN_encode_decode_THEN_round_trips() {
        val value = StringDesc.Raw("key: value # comment")

        val encoded = format.encodeToString(RawStringDescSerializer, value)

        assertEquals(value, format.decodeFromString(RawStringDescSerializer, encoded))
    }

    @Test
    fun GIVEN_color_code_prefix_WHEN_encode_decode_THEN_round_trips() {
        val value = StringDesc.Raw("&aHello &lWorld")

        val encoded = format.encodeToString(RawStringDescSerializer, value)

        assertEquals(value, format.decodeFromString(RawStringDescSerializer, encoded))
    }
}

package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.Serializable
import ru.astrainteractive.astralibs.serialization.SerializerExt.decodeFromString
import ru.astrainteractive.astralibs.serialization.SerializerExt.encodeToString
import ru.astrainteractive.astralibs.serialization.SerializerExt.parse
import ru.astrainteractive.astralibs.serialization.SerializerExt.writeIntoFile
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class YamlSerializerTest {

    private inline fun <reified T> assertEncodeDecode(serializer: Serializer, value: T) {
        val encodedString = serializer.encodeToString(value)
        val decodedValue = serializer.decodeFromString<T>(encodedString)
        assertEquals(value, decodedValue)
    }

    private inline fun <reified T> assertWriteReadFile(serializer: Serializer, value: T) {
        val file = File.createTempFile(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        serializer.writeIntoFile(value, file)
        assertEquals(value, serializer.parse<T>(file).getOrThrow())
    }

    @JvmInline
    @Serializable
    value class VClass(val value: Int = 0)

    @Serializable
    data class DClass(
        val string: String = UUID.randomUUID().toString(),
        val int: Int = 10
    )

    @Serializable
    data object DObject

    @Test
    fun GIVEN_classes_WHEN_read_write_THEN_success() {
        val serializer: Serializer = YamlSerializer()
        assertEncodeDecode(serializer, 1)
        assertEncodeDecode(serializer, 1L)
        assertEncodeDecode(serializer, "Hello String")
        assertEncodeDecode(serializer, 1.toByte())
        assertEncodeDecode(serializer, listOf("Hello list"))
        assertEncodeDecode(serializer, VClass())
        assertEncodeDecode(serializer, DClass())
        assertEncodeDecode(serializer, DObject)
    }

    @Test
    fun GIVEN_file_WHEN_read_write_THEN_success() {
        val serializer: Serializer = YamlSerializer()
        assertWriteReadFile(serializer, 1)
        assertWriteReadFile(serializer, 1L)
        assertWriteReadFile(serializer, "Hello String")
        assertWriteReadFile(serializer, 1.toByte())
        assertWriteReadFile(serializer, listOf("Hello list"))
        assertWriteReadFile(serializer, VClass())
        assertWriteReadFile(serializer, DClass())
        assertWriteReadFile(serializer, DObject)
    }
}

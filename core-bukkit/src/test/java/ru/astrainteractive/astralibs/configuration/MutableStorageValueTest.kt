package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.YamlConfiguration
import org.junit.Test
import ru.astrainteractive.astralibs.configuration.BukkitMutableStorageValue.anyMutableStorageValue
import ru.astrainteractive.klibs.kstorage.util.withDefault
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("TestFunctionName")
class MutableStorageValueTest {

    private inline fun <reified T : Any> testLoadSaveReset(name: String, expectedValue: T) {
        val file = File.createTempFile("prefix", "postfix")
        val configuration = YamlConfiguration.loadConfiguration(file)
        configuration.anyMutableStorageValue<T?>(name).let { storageValue ->
            // Test initial is null
            assertNull(storageValue.getValue())
            // Test after save value is as expected
            storageValue.save(expectedValue)
            assertEquals(expectedValue, storageValue.getValue())
            // Test after reset value is null
            storageValue.reset()
            assertNull(storageValue.getValue())
            // Test withDefault value is as expected
            assertEquals(expectedValue, storageValue.withDefault { expectedValue }.getValue())
        }
    }

    @Test
    fun GIVEN_primitives_WHEN_save_load_THEN_success() {
        // Primitive
        testLoadSaveReset("string", "Hello world")
        testLoadSaveReset("int", 10)
        testLoadSaveReset("boolean", false)
        testLoadSaveReset("double", 10.0)
        testLoadSaveReset("float", 10f)
        testLoadSaveReset("long", 10L)
    }

    @Test
    fun GIVEN_primitives_lists_WHEN_save_load_THEN_success() {
        testLoadSaveReset("stringList", listOf("1", "2"))
        testLoadSaveReset("intList", listOf(1, 2))
        testLoadSaveReset("boolList", listOf(true, false))
        testLoadSaveReset("doubleList", listOf(1.0, 2.0))
        testLoadSaveReset("floatList", listOf(1f, 2f))
        testLoadSaveReset("longList", listOf(1L, 2L))
        testLoadSaveReset("byteList", listOf(Byte.MAX_VALUE, Byte.MIN_VALUE))
        testLoadSaveReset("charList", listOf('1', '2'))
        testLoadSaveReset("shortList", listOf(Short.MAX_VALUE, Short.MIN_VALUE))
    }

    @JvmInline
    value class TestValueClass(val value: Int)

    @Test
    fun GIVEN_value_class_WHEN_save_load_THEN_success() {
        testLoadSaveReset("valueClass", TestValueClass(10))
    }

    data class TestDataClass(
        val valueInt: Int,
        val valueString: String,
        val valueList: List<Int>
    )

    @Test
    fun GIVEN_data_class_WHEN_save_load_THEN_success() {
        testLoadSaveReset(
            name = "dataClass",
            expectedValue = TestDataClass(
                valueInt = 10,
                valueString = "HelloWorld",
                valueList = listOf(1, 2, 3)
            )
        )
    }
}

package ru.astrainteractive.astralibs.krate

import kotlinx.serialization.Serializable
import org.junit.Test
import ru.astrainteractive.astralibs.krate.core.StringFormatKrate
import ru.astrainteractive.astralibs.krate.util.KrateExt.delete
import ru.astrainteractive.astralibs.serialization.YamlStringFormat
import ru.astrainteractive.klibs.kstorage.update
import java.io.File
import java.util.UUID
import kotlin.random.Random
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class TextKrateTest {

    private inline fun <T> withTempFolder(block: (File) -> T): T {
        val folder = File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString())
        if (folder.exists()) folder.delete()
        folder.mkdirs()
        val result = block.invoke(folder)
        folder.delete()
        return result
    }

    @Serializable
    private data class TestStorage(
        val name: String = UUID.randomUUID().toString(),
        val date: String = UUID.randomUUID().toString(),
        val age: Int = Random.nextInt()
    )

    @Test
    fun GIVEN_cache_storage_WHEN_serialized_THEN_success() = withTempFolder { folder ->
        val initial = TestStorage()
        val serializer = YamlStringFormat()
        val storage = StringFormatKrate(
            stringFormat = serializer,
            kSerializer = TestStorage.serializer(),
            factory = { initial },
            fileName = UUID.randomUUID().toString(),
            folder = folder
        )
        storage.delete()
        assertEquals(initial, storage.value)
        storage.load()
        assertEquals(initial, storage.value)
        val newValue = storage.value.copy(age = Random.nextInt())
        storage.update { newValue }
        assertEquals(newValue, storage.value)
    }

    @Test
    fun GIVEN_yaml_storage_provider_WHEN_serialized_THEN_success() = withTempFolder { folder ->
        val initial = TestStorage()
        val storage = StringFormatKrate(
            stringFormat = YamlStringFormat(),
            kSerializer = TestStorage.serializer(),
            factory = { initial },
            fileName = "yaml_test_file.yaml",
            folder = folder
        )
        assertEquals(initial, storage.value)
        storage.load()
        assertEquals(initial, storage.value)
        val newValue = storage.value.copy(age = Random.nextInt())
        storage.update { newValue }
        assertEquals(newValue, storage.value)
    }
}

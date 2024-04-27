package ru.astrainteractive.astralibs.filestorage

import kotlinx.serialization.Serializable
import org.junit.Test
import ru.astrainteractive.astralibs.filestorage.FileStorageExt.delete
import ru.astrainteractive.astralibs.filestorage.FileStorageExt.provide
import ru.astrainteractive.astralibs.serialization.YamlSerializer
import java.io.File
import java.util.UUID
import kotlin.random.Random
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class FileStorageValueTest {

    @Serializable
    private data class TestStorage(
        val name: String = UUID.randomUUID().toString(),
        val date: String = UUID.randomUUID().toString(),
        val age: Int = Random.nextInt()
    )

    @Test
    fun GIVEN_cache_storage_WHEN_serialized_THEN_success() {
        val initial = TestStorage()
        val serializer = YamlSerializer()
        val storage = FileStorageValue(
            serializer = serializer,
            kSerializer = TestStorage.serializer(),
            default = { initial },
            key = UUID.randomUUID().toString(),
            folder = File("./temp")
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
    fun GIVEN_yaml_storage_provider_WHEN_serialized_THEN_success() {
        val initial = TestStorage()
        val storage = YamlFileStorageValueProvider.provide(
            default = { initial },
            key = UUID.randomUUID().toString(),
        )
        assertEquals(initial, storage.value)
        storage.load()
        assertEquals(initial, storage.value)
        val newValue = storage.value.copy(age = Random.nextInt())
        storage.update { newValue }
        assertEquals(newValue, storage.value)
    }
}

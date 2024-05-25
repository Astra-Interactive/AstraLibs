package ru.astrainteractive.aspekt.benchmarks

import com.charleskorn.kaml.Yaml
import kotlinx.benchmark.Scope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import ru.astrainteractive.astralibs.encoding.encoder.JavaObjectEncoder
import ru.astrainteractive.astralibs.krate.core.BinaryFormatKrate
import ru.astrainteractive.astralibs.krate.core.JBinaryKrate
import ru.astrainteractive.astralibs.krate.core.StringFormatKrate
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.test.assertEquals

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
class KtsTestBenchmark {
    private inline fun <T> withTempFolder(block: (File) -> T): T {
        val folder = File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString())
        if (folder.exists()) folder.delete()
        folder.mkdirs()
        val result = block.invoke(folder)
        folder.delete()
        return result
    }

    @Serializable
    data class SomeObject(
        val intValue: Int = Random.nextInt(),
        val stringValue: String = UUID.randomUUID().toString(),
        val map: Map<String, Int> = buildMap {
            repeat(5000) { put(UUID.randomUUID().toString(), Random.nextInt()) }
        },
        val list: List<String> = List(5000) { UUID.randomUUID().toString() },
        val m2ap: Map<String, Int> = buildMap {
            repeat(5000) { put(UUID.randomUUID().toString(), Random.nextInt()) }
        },
        val li2st: List<String> = List(5000) { UUID.randomUUID().toString() },
        val playerName: String = UUID.randomUUID().toString(),
        val uuid: String = UUID.randomUUID().toString(),
        val isSwearFilterEnabled: Boolean = true,
        val player2Name: String = UUID.randomUUID().toString(),
        val uu2id: String = UUID.randomUUID().toString(),
        val isSwea2rFilterEnabled: Boolean = true,
    ) : java.io.Serializable

    @Benchmark
    fun jsonKrateBenchmark(): SomeObject = withTempFolder { currentFolder ->
        val initialValue = SomeObject()
        val krate = StringFormatKrate(
            stringFormat = Json,
            kSerializer = SomeObject.serializer(),
            default = initialValue,
            fileName = "json_format.yaml",
            folder = currentFolder
        )
        assertEquals(initialValue, krate.load())
        val newValue = SomeObject()
        krate.save(newValue)
        assertEquals(newValue, krate.load())
        return krate.value
    }

    @Benchmark
    fun yamlKrateBenchmark(): SomeObject = withTempFolder { currentFolder ->
        val initialValue = SomeObject()
        val krate = StringFormatKrate(
            stringFormat = Yaml.default,
            kSerializer = SomeObject.serializer(),
            default = initialValue,
            fileName = "yaml_format.yaml",
            folder = currentFolder
        )
        assertEquals(initialValue, krate.load())
        val newValue = SomeObject()
        krate.save(newValue)
        assertEquals(newValue, krate.load())
        return krate.value
    }

    @Benchmark
    fun javaIOKrateBenchmark(): SomeObject = withTempFolder { currentFolder ->
        val initialValue = SomeObject()
        val krate = JBinaryKrate(
            default = initialValue,
            objectEncoder = JavaObjectEncoder(),
            fileName = "java_io.bin",
            folder = currentFolder
        )
        assertEquals(initialValue, krate.load())
        val newValue = SomeObject()
        krate.save(newValue)
        assertEquals(newValue, krate.load())
        return krate.value
    }

    @Benchmark
    fun protoBufKrateBenchmark(): SomeObject = withTempFolder { currentFolder ->
        val initialValue = SomeObject()
        val krate = BinaryFormatKrate(
            factory = initialValue,
            key = "proto_krate.proto",
            folder = currentFolder,
            binaryFormat = ProtoBuf,
            kSerializer = SomeObject.serializer()
        )
        assertEquals(initialValue, krate.load())
        val newValue = SomeObject()
        krate.save(newValue)
        assertEquals(newValue, krate.load())
        return krate.value
    }
}

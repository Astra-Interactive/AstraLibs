package ru.astrainteractive.astralibs.util

import kotlinx.serialization.StringFormat
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class StringFormatExtTest {

    private val format: StringFormat = YamlStringFormat()

    private val unparseableYaml = """
        - not
        - an
        - object
    """.trimIndent()

    private fun newTempDir(): File = Files.createTempDirectory("astralibs-test").toFile()

    @Test
    fun GIVEN_missing_file_WHEN_parse_THEN_failure() {
        val file = File(newTempDir(), "missing.yml")

        assertTrue(format.parse<PersistedConfig>(file).isFailure)
    }

    @Test
    fun GIVEN_valid_file_WHEN_parse_THEN_success_with_value() {
        val file = File(newTempDir(), "config.yml")
        val value = PersistedConfig(name = "server", count = 7)
        format.writeIntoFile(value, file)

        assertEquals(value, format.parse<PersistedConfig>(file).getOrThrow())
    }

    @Test
    fun GIVEN_unparseable_content_WHEN_parse_THEN_failure() {
        val file = File(newTempDir(), "broken.yml")
        file.writeText(unparseableYaml)

        assertTrue(format.parse<PersistedConfig>(file).isFailure)
    }

    @Test
    fun GIVEN_missing_file_WHEN_parse_or_null_THEN_null() {
        val file = File(newTempDir(), "missing.yml")

        assertNull(format.parseOrNull<PersistedConfig>(file))
    }

    @Test
    fun GIVEN_valid_file_WHEN_parse_or_null_THEN_value() {
        val file = File(newTempDir(), "config.yml")
        val value = PersistedConfig(name = "lobby", count = 3)
        format.writeIntoFile(value, file)

        assertEquals(value, format.parseOrNull<PersistedConfig>(file))
    }

    @Test
    fun GIVEN_missing_file_WHEN_parse_or_default_THEN_factory_value() {
        val file = File(newTempDir(), "missing.yml")
        val fallback = PersistedConfig(name = "fallback", count = 1)

        assertEquals(fallback, format.parseOrDefault(file) { fallback })
    }

    @Test
    fun GIVEN_empty_file_WHEN_parse_or_default_THEN_factory_value() {
        val file = File(newTempDir(), "empty.yml")
        file.createNewFile()
        val fallback = PersistedConfig(name = "fallback", count = 1)

        assertEquals(fallback, format.parseOrDefault(file) { fallback })
    }

    @Test
    fun GIVEN_valid_file_WHEN_parse_or_default_THEN_parsed_value_and_factory_not_invoked() {
        val file = File(newTempDir(), "config.yml")
        val value = PersistedConfig(name = "stored", count = 42)
        format.writeIntoFile(value, file)

        val result = format.parseOrDefault<PersistedConfig>(file) { error("factory must not be invoked") }

        assertEquals(value, result)
    }

    @Test
    fun GIVEN_unparseable_file_WHEN_parse_or_default_THEN_factory_value() {
        val file = File(newTempDir(), "broken.yml")
        file.writeText(unparseableYaml)
        val fallback = PersistedConfig(name = "fallback", count = 1)

        assertEquals(fallback, format.parseOrDefault(file) { fallback })
    }

    @Test
    fun GIVEN_nested_missing_path_WHEN_write_into_file_THEN_creates_dirs_and_file() {
        val file = File(newTempDir(), "nested/deep/config.yml")
        val value = PersistedConfig(name = "deep", count = 9)

        format.writeIntoFile(value, file)

        assertTrue(file.exists())
        assertEquals(value, format.parse<PersistedConfig>(file).getOrThrow())
    }

    @Test
    fun GIVEN_valid_file_WHEN_parse_or_write_into_default_THEN_returns_parsed_value() {
        val file = File(newTempDir(), "config.yml")
        val stored = PersistedConfig(name = "stored", count = 5)
        format.writeIntoFile(stored, file)

        val result = format.parseOrWriteIntoDefault(file) { PersistedConfig(name = "default", count = 0) }

        assertEquals(stored, result)
    }

    @Test
    fun GIVEN_missing_file_WHEN_parse_or_write_into_default_THEN_writes_default_into_same_file() {
        val file = File(newTempDir(), "config.yml")
        val default = PersistedConfig(name = "default", count = 0)

        val result = format.parseOrWriteIntoDefault(file) { default }

        assertEquals(default, result)
        assertTrue(file.exists())
        assertEquals(default, format.parse<PersistedConfig>(file).getOrThrow())
    }

    @Test
    fun GIVEN_unparseable_file_WHEN_parse_or_write_into_default_THEN_writes_sibling_default_file() {
        val dir = newTempDir()
        val file = File(dir, "config.yml")
        file.writeText(unparseableYaml)
        val default = PersistedConfig(name = "default", count = 0)

        val result = format.parseOrWriteIntoDefault(file) { default }

        assertEquals(default, result)
        val siblingDefault = File(dir, "config.default.yml")
        assertTrue(siblingDefault.exists())
        assertEquals(default, format.parse<PersistedConfig>(siblingDefault).getOrThrow())
        assertTrue(format.parse<PersistedConfig>(file).isFailure)
    }

    @Test
    fun GIVEN_missing_file_WHEN_krate_get_value_THEN_returns_default_and_creates_file() {
        val file = File(newTempDir(), "config.yml")
        val default = PersistedConfig(name = "default", count = 0)
        val krate = format.krateOf(file, ValueFactory { default })

        val value = krate.getValue()

        assertEquals(default, value)
        assertTrue(file.exists())
    }

    @Test
    fun GIVEN_saved_value_WHEN_krate_get_value_THEN_returns_saved_value() {
        val file = File(newTempDir(), "config.yml")
        val default = PersistedConfig(name = "default", count = 0)
        val krate = format.krateOf(file, ValueFactory { default })
        val saved = PersistedConfig(name = "saved", count = 99)

        krate.save(saved)

        assertEquals(saved, krate.getValue())
    }

    @Test
    fun GIVEN_nullable_krate_WHEN_save_null_THEN_deletes_file_and_returns_default() {
        val file = File(newTempDir(), "config.yml")
        val default = PersistedConfig(name = "default", count = 0)
        val krate = format.krateOf<PersistedConfig?>(file, ValueFactory { default })
        krate.save(PersistedConfig(name = "saved", count = 1))
        assertTrue(file.exists())

        krate.save(null)

        assertFalse(file.exists())
        assertEquals(default, krate.getValue())
    }

    @Test
    fun GIVEN_empty_file_WHEN_parse_THEN_failure() {
        val file = File(newTempDir(), "empty.yml")
        file.createNewFile()

        assertTrue(format.parse<PersistedConfig>(file).isFailure)
    }

    @Test
    fun GIVEN_blank_file_WHEN_parse_THEN_failure() {
        val file = File(newTempDir(), "blank.yml")
        file.writeText("   ")

        assertTrue(format.parse<PersistedConfig>(file).isFailure)
    }

    @Test
    fun GIVEN_unparseable_content_WHEN_parse_or_null_THEN_null() {
        val file = File(newTempDir(), "broken.yml")
        file.writeText(unparseableYaml)

        assertNull(format.parseOrNull<PersistedConfig>(file))
    }

    @Test
    fun GIVEN_blank_non_empty_file_WHEN_parse_or_default_THEN_factory_value() {
        val file = File(newTempDir(), "blank.yml")
        file.writeText("   ")
        val fallback = PersistedConfig(name = "fallback", count = 1)

        assertEquals(fallback, format.parseOrDefault(file) { fallback })
    }

    @Test
    fun GIVEN_existing_file_WHEN_overwritten_with_shorter_content_THEN_truncated() {
        val file = File(newTempDir(), "config.yml")
        format.writeIntoFile(PersistedConfig(name = "verylongservername", count = 999999), file)

        format.writeIntoFile(PersistedConfig(name = "x", count = 0), file)

        assertEquals(PersistedConfig(name = "x", count = 0), format.parse<PersistedConfig>(file).getOrThrow())
        assertFalse(file.readText().contains("verylongservername"))
    }

    @Test
    fun GIVEN_file_with_unknown_keys_WHEN_parse_or_write_into_default_THEN_rewritten_without_them() {
        val dir = newTempDir()
        val file = File(dir, "config.yml")
        file.writeText(
            """
            name: server
            count: 5
            legacyField: 99
            """.trimIndent()
        )

        val result = format.parseOrWriteIntoDefault(file) { PersistedConfig(name = "default", count = 0) }

        assertEquals(PersistedConfig(name = "server", count = 5), result)
        assertFalse(file.readText().contains("legacyField"))
    }

    @Test
    fun GIVEN_krate_WHEN_file_changes_externally_THEN_get_value_reflects_change() {
        val file = File(newTempDir(), "config.yml")
        val krate = format.krateOf(file, ValueFactory { PersistedConfig(name = "default", count = 0) })
        krate.save(PersistedConfig(name = "first", count = 1))
        assertEquals(PersistedConfig(name = "first", count = 1), krate.getValue())

        format.writeIntoFile(PersistedConfig(name = "external", count = 2), file)

        assertEquals(PersistedConfig(name = "external", count = 2), krate.getValue())
    }

    @Test
    fun GIVEN_krate_WHEN_saved_twice_THEN_latest_value_returned() {
        val file = File(newTempDir(), "config.yml")
        val krate = format.krateOf(file, ValueFactory { PersistedConfig(name = "default", count = 0) })

        krate.save(PersistedConfig(name = "first", count = 1))
        krate.save(PersistedConfig(name = "second", count = 2))

        assertEquals(PersistedConfig(name = "second", count = 2), krate.getValue())
    }
}

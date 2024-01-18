package ru.astrainteractive.astralibs.filemanager

import org.junit.jupiter.api.assertThrows
import ru.astrainteractive.astralibs.filemanager.impl.JVMResourceFileManager
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class JVMResourceFileManagerTest {
    @Test
    fun GIVEN_existing_res_file_WHEN_try_read_string_content_THEN_content_equals_from_resources() {
        JVMResourceFileManager("SampleFile.txt", File("."), JVMFileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("Hello World", stringContent)
            it.configFile.delete()
        }
    }

    @Test
    fun GIVEN_existing_res_file_inside_folder_WHEN_try_read_string_content_THEN_content_equals_from_resources() {
        JVMResourceFileManager("folder/FileInFolder.txt", File("."), JVMFileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("File In Folder", stringContent)
            it.configFile.delete()
        }
    }

    @Test
    fun GIVEN_not_existing_res_file_WHEN_try_read_THEN_exception() {
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            JVMResourceFileManager("note_exists_file", File("."), JVMFileManagerTest::class.java).also {
                it.configFile.readText()
            }
        }
    }
}

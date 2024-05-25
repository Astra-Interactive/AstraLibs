package ru.astrainteractive.astralibs.filemanager

import ru.astrainteractive.astralibs.filemanager.impl.JVMFileManager
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class JVMFileManagerTest {
    @Test
    fun GIVEN_not_existing_file_WHEN_call_from_jvmfilemanager_THEN_exists() {
        JVMFileManager("test.json", File(".")).also {
            assertTrue(it.configFile.exists())
            it.configFile.delete()
        }
    }
}

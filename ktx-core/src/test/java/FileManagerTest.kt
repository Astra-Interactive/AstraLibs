import org.junit.jupiter.api.assertThrows
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import ru.astrainteractive.astralibs.filemanager.impl.JVMFileManager
import ru.astrainteractive.astralibs.filemanager.impl.JVMResourceFileManager
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileManagerTest {
    @Test
    fun `Testing saving with DefaultFileManager`() {
        JVMFileManager("test.json", File(".")).also {
            assertTrue(it.configFile.exists())
            it.configFile.delete()
        }

        JVMResourceFileManager("SampleFile.txt", File("."), FileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("Hello World", stringContent)
            it.configFile.delete()
        }

        JVMResourceFileManager("folder/FileInFolder.txt", File("."), FileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("File In Folder", stringContent)
            it.configFile.delete()
        }
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            JVMResourceFileManager("note_exists_file", File("."), FileManagerTest::class.java)
        }
    }
}

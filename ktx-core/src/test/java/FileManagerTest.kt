import org.junit.jupiter.api.assertThrows
import ru.astrainteractive.astralibs.filemanager.FileManager
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileManagerTest {
    @Test
    fun `Testing saving with DefaultFileManager`() {
        FileManager("test.json", File(".")).also {
            assertTrue(it.configFile.exists())
            it.configFile.delete()
        }

        ResourceFileManager("SampleFile.txt", File("."), FileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("Hello World", stringContent)
            it.configFile.delete()
        }

        ResourceFileManager("folder/FileInFolder.txt", File("."), FileManagerTest::class.java).also {
            val stringContent = it.configFile.readText()
            assertEquals("File In Folder", stringContent)
            it.configFile.delete()
        }
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            ResourceFileManager("note_exists_file", File("."), FileManagerTest::class.java)
        }
    }
}
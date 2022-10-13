import ru.astrainteractive.astralibs.file_manager.FileManager
import ru.astrainteractive.astralibs.file_manager.IResourceProvider
import org.junit.jupiter.api.Test
import java.io.File

class CustomResourceProvider(val dataFolder: File) : IResourceProvider {
    override fun isResourceExists(fileName: String): Boolean {
        return File(dataFolder, fileName).exists()
    }

    override fun saveFromResource(fileName: String): File {
        val file = File(dataFolder, fileName)
        file.createNewFile()
        return file
    }

}

class FileManagerTest {
    val parentFolder = "D:\\Minecraft Servers\\AstraInteractive-Git\\plugins\\AstraLibs"

    private val pluginFolderPath: File by lazy {
        File(parentFolder)
    }
    private val sep: String
        get() = File.separator
    private val resourcesFolder by lazy {
        File(pluginFolderPath, "src${sep}test${sep}resources")
    }
    val mockFile1: File by lazy {
        File(resourcesFolder, "mock_1.yml")
    }
    val resourceProvider by lazy {
        CustomResourceProvider(resourcesFolder)
    }

    @Test
    fun tests() {
        println(pluginFolderPath.absolutePath)
        val existedFile = FileManager("mock_1.yml", resourcesFolder, resourceProvider)

    }
}
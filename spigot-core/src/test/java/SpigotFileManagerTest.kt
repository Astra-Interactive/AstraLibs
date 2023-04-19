import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.junit.jupiter.api.assertThrows
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.file_manager.SpigotFileManager
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.assertEquals


class SpigotFileManagerTest {
    private lateinit var server: ServerMock;
    private lateinit var plugin: JavaPlugin;

    @BeforeTest
    public fun setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin()
        AstraLibs.rememberPlugin(plugin)

    }

    @AfterTest
    public fun tearDown() {
        MockBukkit.unmock();
    }

    @Test
    fun sample() {
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            SpigotFileManager("test.yml", plugin.dataFolder)
        }
    }
}
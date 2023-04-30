package ru.astrainteractive.astralibs.filemanager

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.bukkit.plugin.java.JavaPlugin
import org.junit.jupiter.api.assertThrows
import ru.astrainteractive.astralibs.filemanager.DefaultSpigotFileManager
import ru.astrainteractive.astralibs.filemanager.SpigotFileManager
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.AfterTest


class SpigotFileManagerTest {
    private lateinit var server: ServerMock;
    private lateinit var plugin: JavaPlugin;

    @BeforeTest
    public fun setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin()
    }

    @AfterTest
    public fun tearDown() {
        MockBukkit.unmock();
    }

    @Test
    fun sample() {
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            DefaultSpigotFileManager(plugin, "test.yml", plugin.dataFolder)
        }
    }
}
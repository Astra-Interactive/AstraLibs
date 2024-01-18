package ru.astrainteractive.astralibs.filemanager

import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class FileConfigurationManagerTest {

    private fun createMockPlugin(): Plugin {
        val plugin: Plugin = mock(Plugin::class.java)
        `when`(plugin.dataFolder).thenReturn(File.createTempFile("prefix", "postfix").parentFile)
        return plugin
    }

    @Test
    fun GIVEN_not_existing_required_resource_WHEN_call_THEN_failure() {
        val plugin = createMockPlugin()
        assertThrows<ResourceFileManager.Exception.ResourceNotExists> {
            DefaultFileConfigurationManager(
                plugin = plugin,
                name = "${UUID.randomUUID()}.yml",
                dataFolder = plugin.dataFolder,
                isResourceRequired = true
            ).configFile
        }
    }

    @Test
    fun GIVEN_not_existing_non_required_resource_WHEN_call_THEN_success() {
        val plugin = createMockPlugin()
        val configManager = DefaultFileConfigurationManager(
            plugin = plugin,
            name = "${UUID.randomUUID()}.yml",
            dataFolder = plugin.dataFolder,
            isResourceRequired = false
        )
        assertFalse(configManager.isResourceExists)
        assertTrue(configManager.configFile.exists())
    }
}

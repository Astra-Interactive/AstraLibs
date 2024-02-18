package ru.astrainteractive.astralibs.economy

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.klibs.kdi.Factory

class EconomyProviderFactory(private val rootPlugin: JavaPlugin) : Factory<EconomyProvider> {

    private fun interface EconomyConstructor {
        fun create(rootPlugin: JavaPlugin, plugin: Plugin): EconomyProvider
    }

    private fun tryGetEconomyProvider(pluginName: String, constructor: EconomyConstructor): EconomyProvider? {
        val plugin = Bukkit.getPluginManager().getPlugin(pluginName) ?: run {
            Bukkit.getLogger().info("Could not got Vault EconomyProvider")
            return null
        }
        return kotlin.runCatching { constructor.create(rootPlugin, plugin) }
            .onFailure { Bukkit.getLogger().info("Could not got Vault EconomyProvider") }
            .getOrNull()
    }

    override fun create(): EconomyProvider {
        val providers = listOfNotNull(
            tryGetEconomyProvider("Vault", ::VaultEconomyProvider),
            tryGetEconomyProvider("Essentials", ::EssentialsEconomyProvider)
        )
        return providers.firstOrNull() ?: let {
            Bukkit.getLogger().info("Economy providers is empty!")
            EmptyEconomyProvider()
        }
    }
}

package ru.astrainteractive.astralibs.economy

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.klibs.kdi.Provider
import ru.astrainteractive.klibs.kdi.getValue
import java.util.UUID

class AnyEconomyProvider(private val rootPlugin: JavaPlugin) : EconomyProvider {
    private val compatibilities = HashSet<EconomyProvider>()
    private val compatibility by Provider {
        compatibilities.first()
    }

    override fun getBalance(uuid: UUID): Double? {
        return compatibility.getBalance(uuid)
    }

    override fun takeMoney(uuid: UUID, amount: Double): Boolean {
        return compatibility.takeMoney(uuid, amount)
    }

    override fun addMoney(uuid: UUID, amount: Double): Boolean {
        return compatibility.addMoney(uuid, amount)
    }

    override fun hasAtLeast(uuid: UUID, amount: Double): Boolean {
        return compatibility.hasAtLeast(uuid, amount)
    }

    private fun interface EconomyConstructor {
        fun create(rootPlugin: JavaPlugin, plugin: Plugin): EconomyProvider
    }

    private fun handleCompatibility(pluginName: String, constructor: EconomyConstructor) {
        val plugin = Bukkit.getPluginManager().getPlugin(pluginName) ?: return
        val economyProvider = constructor.create(rootPlugin, plugin)
        compatibilities.add(economyProvider)
    }

    private fun onCreated() {
        handleCompatibility("Vault") { rootPlugin, plugin ->
            Bukkit.getLogger().info("Got Vault EconomyProvider")
            VaultEconomyProvider(rootPlugin, plugin)
        }
        if (compatibilities.isNotEmpty()) return

        handleCompatibility("Essentials") { rootPlugin, plugin ->
            Bukkit.getLogger().info("Got Essentials EconomyProvider")
            EssentialsEconomyProvider(rootPlugin, plugin)
        }
    }

    init {
        onCreated()
    }
}

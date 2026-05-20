package ru.astrainteractive.astralibs.lifecycle

import org.bukkit.plugin.java.JavaPlugin

/** [JavaPlugin] base class that also satisfies the [Lifecycle] contract. */
abstract class LifecyclePlugin : JavaPlugin(), Lifecycle {
    override fun onEnable() {
        super<JavaPlugin>.onEnable()
        super<Lifecycle>.onEnable()
    }

    override fun onDisable() {
        super<JavaPlugin>.onDisable()
        super<Lifecycle>.onDisable()
    }
}

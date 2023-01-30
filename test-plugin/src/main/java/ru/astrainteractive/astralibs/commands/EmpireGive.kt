package ru.astrainteractive.astralibs.commands

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.AstraLibs

// egive <player> <item> [amount]
object EmpireGive {

    fun register() = AstraLibs.instance.registerCommand("egive") {
        val player = argument(0) {
            it?.let(Bukkit::getPlayer)
        }.onFailure {
            sender.sendMessage("Player not exists: $it")
        }.successOrNull()?.value ?: return@registerCommand

        val itemStack = argument(1) {
            it?.let(Material::getMaterial)?.let(::ItemStack)
        }.onFailure {
            sender.sendMessage("ItemStack not exists: $it")
        }.successOrNull()?.value ?: return@registerCommand

        val amount = argument(2) {
            it?.toIntOrNull() ?: 1
        }.onSuccess {
            val amount = it.value
            sender.sendMessage("Player ${player.name} received ${amount} ${itemStack.type.name}")
            player.inventory.addItem(itemStack.apply { this.amount = amount })
        }
    }

    fun tabComplemter() = AstraLibs.instance.registerTabCompleter("egive") {
        return@registerTabCompleter listOf()
    }
}
package ru.astrainteractive.astralibs.commands

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.AstraLibs

// egive <player> <item> [amount]
object EmpireGive {

    fun register() = AstraLibs.instance.registerCommand("egive") { player() }

    private fun Command.player() = argument(
        index = 0,
        parser = { it?.let(Bukkit::getPlayer) },
        onError = {
            sender.sendMessage("Player not exists: $it")
        },
        onResult = { itemStack(it.value) }
    )

    private fun Command.itemStack(player: Player) = argument(
        1,
        parser = { it?.let(Material::getMaterial)?.let(::ItemStack) },
        onError = {
            sender.sendMessage("ItemStack not exists: $it")
        },
        onResult = {
            val item = it.value
            amount(player, item)
        }
    )

    private fun Command.amount(player: Player, item: ItemStack) = argument(
        2,
        parser = { it?.toIntOrNull() ?: 0 },
        onResult = {
            val amount = it.value
            sender.sendMessage("Player ${player.name} received ${amount} ${item.type.name}")
            player.inventory.addItem(item.apply { this.amount = amount })
        }
    )

    fun tabComplemter() = AstraLibs.instance.registerTabCompleter("egive"){
        return@registerTabCompleter listOf()
    }
}
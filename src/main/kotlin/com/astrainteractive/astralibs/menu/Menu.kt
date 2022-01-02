package com.astrainteractive.astralibs.menu

import com.astrainteractive.astralibs.callSyncMethod
import com.astrainteractive.astralibs.runAsyncTask
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder


/**
 * PlayerMenuUtility data class
 *
 * Don't use just Player class
 */
open class AstraPlayerMenuUtility(open var player: Player)
enum class AstraMenuSize(val size:Int){
    XXS(9),XS(18),S(27),M(36),L(45),XL(54)
}
/**
 * Default menu abstract class
 */
abstract class Menu() : InventoryHolder {
    abstract val playerMenuUtility: AstraPlayerMenuUtility

    private lateinit var inventory: Inventory
    fun isInventoryInitialized() = this::inventory.isInitialized

    /**
     * Title of this inventory
     */
    abstract var menuName: String

    /**
     * Size of inventory
     *
     * Shoul be in [9;54] and divided by 9
     */
    abstract val menuSize: AstraMenuSize

    /**
     * Menu handler
     */
    abstract fun handleMenu(e: InventoryClickEvent)

    /**
     * Function for setting items in menu
     */
    abstract fun setMenuItems()

    /**
     * Open inventory method for Menu class
     */
    fun open() {
        runAsyncTask {
            inventory = Bukkit.createInventory(this, menuSize.size, menuName)
            setMenuItems()
            callSyncMethod {
                playerMenuUtility.player.openInventory(inventory)
            }
        }
    }

    override fun getInventory() = inventory

}
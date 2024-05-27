package chch.p2winventory

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType

class EventListener : Listener {
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val databaseManager = P2WInventory.instance!!.databaseManager
        val player = event.player
        val playerActiveSlots = databaseManager.getActiveSlots(player)
        if (playerActiveSlots > 9) {
            for (i in 35 downTo 0+playerActiveSlots) {
                player.inventory.setItem(i, P2WInventory.instance!!.getUnavailableItem())
            }
        }
    }
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.itemDrop.itemStack)) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.currentItem!!)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.item!!)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return

        val player = event.entity as Player

        if (event.damage < player.health) return

        for (item: ItemStack? in player.inventory.contents) {
            if (item != null) {
                if (P2WInventory.instance!!.itemIsUnavailable(item)) {
                    player.inventory.remove(item)
                }
            }
        }
    }
}
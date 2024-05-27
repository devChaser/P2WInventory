package chch.p2winventory

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack

class EventListener : Listener {
    val databaseManager = P2WInventory.instance!!.databaseManager
    // Block a slot if player dies
    // TODO: CONFIG
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        databaseManager.revokeActiveSlots(event.entity)
        event.entity.sendMessage("§7[§cP§e2§aW§bI§7] §c》 §f§lYou §kkk§r§l§4lost§kkk§r§l §fan inventory slot.")
    }

    // Block unavailable slots when player respawns
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val playerActiveSlots = databaseManager.getActiveSlots(player)
        if (playerActiveSlots > 9) {
            for (i in 35 downTo 0+playerActiveSlots) {
                player.inventory.setItem(i, P2WInventory.instance!!.getUnavailableItem())
            }
        }
    }
    // Don't drop slotblockers®️
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.itemDrop.itemStack)) {
            event.isCancelled = true
        }
    }
    // Can't drop/move/remove slotblockers®️ from your inventory
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.currentItem)) {
            event.isCancelled = true
        }
    }
    // Can't place slotblockers®️ from your inventory
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.item)) {
            event.isCancelled = true
        }
    }
    // Don't drop slotblockers®️ on player's death
    // TODO: CHANGE TO PlayerDeathEvent
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
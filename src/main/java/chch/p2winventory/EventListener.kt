package chch.p2winventory

import org.bukkit.Bukkit
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

    // Block unavailable slots when player respawns
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val playerActiveSlots = databaseManager.getActiveSlots(player)
        for (i in 35 downTo 0+playerActiveSlots) {
            player.inventory.setItem(i, P2WInventory.instance!!.getUnavailableItem())
        }
    }
    // Don't drop slotblockers®️
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        event.isCancelled = P2WInventory.instance!!.itemIsUnavailable(event.itemDrop.itemStack)
    }
    // Can't drop/move/remove slotblockers®️ from your inventory
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.currentItem == null) return
        event.isCancelled = P2WInventory.instance!!.itemIsUnavailable(event.currentItem!!)
    }
    // Can't place slotblockers®️ from your inventory
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item == null) return
        event.isCancelled = P2WInventory.instance!!.itemIsUnavailable(event.item!!)
    }
    // Don't drop slotblockers®️ on player's death
    // Block a slot if player dies
    // TODO: CONFIG
    // todo: replace with PlayerDeathEvent (if possible)
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player

        if (event.damage < player.health) return

        databaseManager.revokeActiveSlots(player)

        for (item: ItemStack? in player.inventory.contents) {
            if (item == null) continue
            if (P2WInventory.instance!!.itemIsUnavailable(item)) {
                player.inventory.remove(item)
            }
        }
    }
}
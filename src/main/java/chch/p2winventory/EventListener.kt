package chch.p2winventory

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack


class EventListener : Listener {
    private val databaseManager = P2WInventory.instance!!.databaseManager

    // Block unavailable slots when player respawns
    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        P2WInventory.instance!!.giveSlotBlockers(player)
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
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player

        if (event.damage < player.health) return

        for (item: ItemStack? in player.inventory.contents) {
            if (item == null) continue
            if (P2WInventory.instance!!.itemIsUnavailable(item)) {
                player.inventory.remove(item)
            }
        }

        databaseManager.revokeActiveSlots(player)
        player.sendMessage("§cP§e2§aW§bI §7/ §cYou lose §b1 slot §cdue to your death")
    }

    // Unpickupable slotblockers
    @EventHandler
    fun onPlayerPickupEvent(event: EntityPickupItemEvent) {
        if (P2WInventory.instance!!.itemIsUnavailable(event.item.itemStack)) {
            event.item.remove()
            event.isCancelled = true
        }
    }

    // Points for killing entities
    // todo: different amount of points for different mobs
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity.killer !is Player) return
        databaseManager.addBalance(event.entity.killer!!, 1)
        event.entity.killer!!.sendMessage("§cP§e2§aW§bI §7/ §rYou got §b1 point §rfor killing §b${event.entity.name}")
    }

    // Points for advancements complete
    // todo: different amount of points for different advancements
    @EventHandler
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        val banKeys = arrayListOf("minecraft:recipes", "blazeandcave:technical")
        if ((banKeys.filter { it in event.advancement.key.toString() }).isNotEmpty()) return

        databaseManager.addBalance(event.player, 5)
        event.player.sendMessage("§cP§e2§aW§bI §7/ §rYou got §b5 points §rfor complete advancement")
    }
}
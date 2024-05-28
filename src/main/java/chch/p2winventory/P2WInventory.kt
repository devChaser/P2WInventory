package chch.p2winventory

import chch.p2winventory.commands.*
import chch.p2winventory.db.DatabaseManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin


class P2WInventory : JavaPlugin() {
    lateinit var databaseManager: DatabaseManager
    private val unavailableTagKey = NamespacedKey(this, "isUnavailable")

    companion object {
        @JvmStatic
        var instance: P2WInventory? = null
            private set
    }
    override fun onEnable() {
        instance = this
        databaseManager = DatabaseManager()

        dataFolder.mkdirs()

        getCommand("getplayer")?.setExecutor(GetPlayerInfoCMD())

        getCommand("add")?.setExecutor(AddDataCMD())
        getCommand("add")?.tabCompleter = AddRemoveDataCompleter()

        getCommand("remove")?.setExecutor(RemoveDataCMD())
        getCommand("remove")?.tabCompleter = AddRemoveDataCompleter()

        getCommand("buyslot")?.setExecutor(BuySlotCMD())

        server.pluginManager.registerEvents(EventListener(), this)

        // ЭТО ГОВНО НУЖНО ПОФИКСИТЬ ЖЕЛАТЕЛЬНО КАК-НИБУДЬ ЧЕРЕЗ ИВЕНТ
        server.scheduler.runTaskTimer(this, Runnable {
            for (world: World in Bukkit.getWorlds()) {
                for (e: Entity in world.entities) {
                    if (e.type != EntityType.ITEM) continue
                    if (itemIsUnavailable((e as Item).itemStack)) e.remove()
                }
            }
        }, 20L, 20L)
    }

    override fun onDisable() {
        databaseManager.disconnect()
    }

    private fun getUnavailableItem(): ItemStack {
        val slotBanItem = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val slotBanItemMeta = slotBanItem.itemMeta
        slotBanItemMeta?.setDisplayName("§cUnavailable slot §7(§cP§e2§aW§bI§7)")
        slotBanItemMeta?.persistentDataContainer?.set(unavailableTagKey, PersistentDataType.BOOLEAN, true)
        slotBanItemMeta?.setMaxStackSize(1)
        slotBanItem.itemMeta = slotBanItemMeta

        return slotBanItem
    }

    fun itemIsUnavailable(item: ItemStack): Boolean {
        if (item.itemMeta == null) return false
        if (item.itemMeta!!.persistentDataContainer.get(unavailableTagKey, PersistentDataType.BOOLEAN) == null) return false
        return item.itemMeta!!.persistentDataContainer.get(unavailableTagKey, PersistentDataType.BOOLEAN)!!
    }

    fun getBuySlotCost(player: Player): Int {
        return databaseManager.getActiveSlots(player) *
                if (databaseManager.getBoughtTimes(player) == 0) 1
                else databaseManager.getBoughtTimes(player)
    }

    fun giveSlotBlockers(player: Player) {
        val playerActiveSlots = databaseManager.getActiveSlots(player)
        for (item: ItemStack? in player.inventory.contents) {
            if (item == null) continue
            if (itemIsUnavailable(item)) {
                player.inventory.remove(item)
            }
        }
        for (i in 9..44-playerActiveSlots) {
            if (player.inventory.getItem(i) != null) {
                player.world.dropItem(player.location, player.inventory.getItem(i)!!)
            }
            player.inventory.setItem(i, getUnavailableItem())
        }
    }
}

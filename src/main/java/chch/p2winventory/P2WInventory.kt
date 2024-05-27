package chch.p2winventory

import chch.p2winventory.commands.addCMD
import chch.p2winventory.commands.addCompleter
import chch.p2winventory.commands.getPlayerInfoCMD
import chch.p2winventory.db.DatabaseManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import javax.xml.crypto.Data

class P2WInventory : JavaPlugin() {
    lateinit var databaseManager: DatabaseManager
    val unavailableTagKey = NamespacedKey(this, "isUnavailable")

    companion object {
        @JvmStatic
        var instance: P2WInventory? = null
            private set
    }
    override fun onEnable() {
        instance = this
        databaseManager = DatabaseManager()

        dataFolder.mkdirs()

        getCommand("getplayer")?.setExecutor(getPlayerInfoCMD())
        getCommand("add")?.setExecutor(addCMD())
        getCommand("add")?.tabCompleter = addCompleter()

        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        databaseManager.disconnect()
    }

    fun getUnavailableItem(): ItemStack {
        val slotBanItem = ItemStack(Material.GLASS_PANE)
        val slotBanItemMeta = slotBanItem.itemMeta
        slotBanItemMeta?.setDisplayName("§cUnavailable slot §7(§cP§e2§aW§bI§7)")
        slotBanItemMeta?.persistentDataContainer?.set(unavailableTagKey, PersistentDataType.BOOLEAN, true)
        slotBanItemMeta?.setMaxStackSize(1)
        slotBanItem.itemMeta = slotBanItemMeta

        return slotBanItem
    }

    fun itemIsUnavailable(item: ItemStack?): Boolean {
        if (item != null) {
            return item.itemMeta!!.persistentDataContainer.get(unavailableTagKey, PersistentDataType.BOOLEAN)!!
        }
        else return false
    }
}

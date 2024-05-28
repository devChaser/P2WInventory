package chch.p2winventory

import chch.p2winventory.commands.*
import chch.p2winventory.db.DatabaseManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

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

        getCommand("getplayer")?.setExecutor(GetPlayerInfoCMD())

        getCommand("add")?.setExecutor(AddDataCMD())
        getCommand("add")?.tabCompleter = AddDataCompleter()

        getCommand("remove")?.setExecutor(RemoveDataCMD())
        getCommand("remove")?.tabCompleter = RemoveDataCompleter()

        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        databaseManager.disconnect()
    }

    fun getUnavailableItem(): ItemStack {
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
}

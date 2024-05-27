package chch.p2winventory

import chch.p2winventory.commands.addCMD
import chch.p2winventory.commands.addCompleter
import chch.p2winventory.commands.getPlayerInfoCMD
import chch.p2winventory.db.DatabaseManager
import org.bukkit.plugin.java.JavaPlugin
import javax.xml.crypto.Data

class P2WInventory : JavaPlugin() {
    lateinit var databaseManager: DatabaseManager

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
    }

    override fun onDisable() {
        databaseManager.disconnect()
    }
}

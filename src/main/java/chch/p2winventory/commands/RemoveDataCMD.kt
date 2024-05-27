package chch.p2winventory.commands

import chch.p2winventory.P2WInventory
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemoveDataCMD : CommandExecutor {
    private val databaseManager = P2WInventory.instance!!.databaseManager
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        sender as Player

        if (args.size < 3) return false

        if (!Bukkit.getOfflinePlayers().contains(Bukkit.getPlayer(args[0]))
                && !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            return true
        }

        val infoPlayer = Bukkit.getPlayer(args[0])!!

        when (args[1]) {
            "activeSlots" -> databaseManager.revokeActiveSlots(infoPlayer, args[2].toInt())
            "boughtTimes" -> databaseManager.revokeBoughtTimes(infoPlayer, args[2].toInt())
            "balance" -> databaseManager.removeBalance(infoPlayer, args[2].toInt())
        }

        return true
    }
}
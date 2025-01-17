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
            "activeSlots" -> {
                databaseManager.revokeActiveSlots(infoPlayer, args[2].toInt())
                P2WInventory.instance!!.giveSlotBlockers(infoPlayer)
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully revoke §b${args[2]} active slots §rfrom §b${infoPlayer.name}")
            }
            "boughtTimes" -> {
                databaseManager.revokeBoughtTimes(infoPlayer, args[2].toInt())
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully revoke §b${args[2]} bought times §rfrom §b${infoPlayer.name}")
            }
            "balance" -> {
                databaseManager.removeBalance(infoPlayer, args[2].toInt())
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully remove §b${args[2]} points §rfrom §b${infoPlayer.name}")
            }
            else -> return false
        }

        return true
    }
}
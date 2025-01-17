package chch.p2winventory.commands

import chch.p2winventory.P2WInventory
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AddDataCMD : CommandExecutor {
    private val databaseManager = P2WInventory.instance!!.databaseManager
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if (args.size < 3) return false

        if (!Bukkit.getOfflinePlayers().contains(Bukkit.getPlayer(args[0]))
                && !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            return true
        }

        val infoPlayer = Bukkit.getPlayer(args[0])!!

        when (args[1]) {
            "activeSlots" -> {
                databaseManager.addActiveSlot(infoPlayer, args[2].toInt())
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully add §b${args[2]} active slots §rto §b${infoPlayer.name}")
                P2WInventory.instance!!.giveSlotBlockers(infoPlayer)
            }
            "boughtTimes" -> {
                databaseManager.addBoughtTimes(infoPlayer, args[2].toInt())
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully add §b${args[2]} bought times §rto §b${infoPlayer.name}")
            }
            "balance" -> {
                databaseManager.addBalance(infoPlayer, args[2].toInt())
                Bukkit.broadcastMessage("§cP§e2§aW§bI §7/ §b${sender.name} §rSuccessfully add §b${args[2]} points §rto §b${infoPlayer.name}")
            }
            else -> return false
        }

        return true
    }
}
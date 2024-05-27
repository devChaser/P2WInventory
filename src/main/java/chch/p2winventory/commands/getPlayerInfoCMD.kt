package chch.p2winventory.commands

import chch.p2winventory.P2WInventory
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class getPlayerInfoCMD : CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        sender as Player
        val databaseManager = P2WInventory.instance!!.databaseManager

        val infoPlayer: Player? = if (args.isNotEmpty()
                        && (Bukkit.getOfflinePlayers().contains(Bukkit.getPlayer(args[0]))
                        || Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))))
                    /* then */
                            Bukkit.getPlayer(args[0])
                    else
                        sender

        infoPlayer!!

        sender.sendMessage("§cP§e2§aW§bI §rPlayer§7: §b${infoPlayer.name}")
        sender.sendMessage("§c》 §rActive slots§7: §b${databaseManager.getActiveSlots(infoPlayer)}")
        sender.sendMessage("§c》 §rBought times§7: §b${databaseManager.getBoughtTimes(infoPlayer)}")
        sender.sendMessage("§c》 §rBalance§7: §b${databaseManager.getBalance(infoPlayer)}")

        return true
    }

}
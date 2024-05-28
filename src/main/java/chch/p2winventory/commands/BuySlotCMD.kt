package chch.p2winventory.commands

import chch.p2winventory.P2WInventory
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuySlotCMD : CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        sender as Player
        val databaseManager = P2WInventory.instance!!.databaseManager
        val instance = P2WInventory.instance!!

        if (databaseManager.getBalance(sender) < instance.getBuySlotCost(sender)) {
            sender.sendMessage("§cP§e2§aW§bI §7/ §cYou don't have enough points")
            return true
        }

        databaseManager.addBoughtTimes(sender)
        databaseManager.addActiveSlot(sender)
        databaseManager.removeBalance(sender, instance.getBuySlotCost(sender))
        P2WInventory.instance!!.giveSlotBlockers(sender)

        sender.sendMessage("§cP§e2§aW§bI §7/ §rSuccessfully bought new slot. §7(§b${databaseManager.getActiveSlots(sender)}§7)")

        return true
    }
}
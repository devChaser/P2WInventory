package chch.p2winventory.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
//import org.bukkit.command.CommandSender
//import org.bukkit.entity.Player
//
//class RemoveCMD : CommandExecutor {
//     fun onCommand(sender: CommandSender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
//        if (args.size < 3) return false
//
//        if (!Bukkit.getOfflinePlayers().contains(Bukkit.getPlayer(args[0]))
//                && !Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
//            return true
//        }
//
//        val infoPlayer = Bukkit.getPlayer(args[0])!!
//
//        when (args[1]) {
//            "activeSlots" -> databaseManager.addActiveSlot(infoPlayer, args[2].toInt())
//            "boughtTimes" -> databaseManager.addBoughtTimes(infoPlayer, args[2].toInt())
//            "balance" -> databaseManager.addBalance(infoPlayer, args[2].toInt())
//        }
//
//        return true
//    }
//}
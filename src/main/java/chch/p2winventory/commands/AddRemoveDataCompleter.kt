package chch.p2winventory.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class AddRemoveDataCompleter : TabCompleter {
    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): MutableList<String> {
        val list = arrayListOf<String>()
        when (p3.size) {
            1 -> {
                for (p: Player in Bukkit.getOnlinePlayers()) {
                    list.add(p.name)
                }
            }
            2 -> list.addAll(listOf("activeSlots", "boughtTimes", "balance"))
        }
        return list
    }
}
package chch.p2winventory.db

import chch.p2winventory.P2WInventory
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
class DatabaseManager {
    private var connection: Connection? = null

    init {
        connect()
        createTable()
    }

    private fun connect() {
        createDatabaseFile()
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:${P2WInventory.instance!!.dataFolder.path}/players_info.db")
        } catch (e: SQLException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
    private fun createDatabaseFile() {
        val databaseFile = File(P2WInventory.instance!!.dataFolder, "players_info.db")

        try {
            databaseFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    public fun disconnect() {
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun createTable() {
        val query = "CREATE TABLE IF NOT EXISTS " +
                "players_info (player_uuid TEXT PRIMARY KEY, activeSlots INTEGER DEFAULT 9, boughtTimes INTEGER DEFAULT 0, balance INTEGER DEFAULT 0)"
        try {
            connection?.createStatement()?.executeUpdate(query)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    public fun getActiveSlots(player: Player): Int {
        addActiveSlot(player,0)
        var activeSlots = 0
        val query = "SELECT activeSlots FROM players_info WHERE player_uuid = '${player.uniqueId}'"
        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return activeSlots
//            statement.setInt(1, player.uniqueId)
            val resultSet: ResultSet = statement.executeQuery()
            if (resultSet.next()) {
                activeSlots = resultSet.getInt("activeSlots")
            }
            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return activeSlots
    }

    public fun getBoughtTimes(player: Player): Int {
        addBoughtTimes(player,0)
        var boughtTimes = 0
        val query = "SELECT boughtTimes FROM players_info WHERE player_uuid = '${player.uniqueId}'"
        try {
            val statement = connection?.prepareStatement(query) ?: return boughtTimes
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                boughtTimes = resultSet.getInt("boughtTimes")
            }

            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return boughtTimes
    }

    public fun getBalance(player: Player): Int {
        addBalance(player,0)
        var balance = 0
        val query = "SELECT balance FROM players_info WHERE player_uuid = '${player.uniqueId}'"
        try {
            val statement = connection?.prepareStatement(query) ?: return balance
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                balance = resultSet.getInt("balance")
            }

            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return balance
    }

    public fun addActiveSlot(player: Player, amount: Int = 1): Boolean {
        if (amount < 0) return false

        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9 + $amount, 0, 0) ON CONFLICT(player_uuid) DO UPDATE SET activeSlots = activeSlots + $amount"
        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return true
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
    }

    public fun addBoughtTimes(player: Player, amount: Int = 1): Boolean {
        if (amount < 0) return false

        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9, $amount, 0) ON CONFLICT(player_uuid) DO UPDATE SET boughtTimes = boughtTimes + $amount"

        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return true
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
            return  true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
    }

    public fun addBalance(player: Player, amount: Int): Boolean {
        if (amount < 0) return false

        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9, 0, $amount) ON CONFLICT(player_uuid) DO UPDATE SET balance = balance + $amount"

        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return true
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
    }

    public fun removeActiveSlots(player: Player, amount: Int = 1) {
        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9, 0, 0) ON CONFLICT(player_uuid) D" +
                "O UPDATE SET activeSlots = ${if (getActiveSlots(player) - amount < 9) "9" else "activeSlots - $amount"}"
        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    public fun removeBoughtTimes(player: Player, amount: Int = 1) {
        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9, 0, 0) ON CONFLICT(player_uuid) " +
                "DO UPDATE SET boughtTimes = ${if (getBoughtTimes(player) - amount < 0) "0" else "boughtTimes - $amount"}"
        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    public fun removeBalance(player: Player, amount: Int) {
        val query = "INSERT INTO players_info (player_uuid, activeSlots, boughtTimes, balance) " +
                "VALUES ('${player.uniqueId}', 9, 0, 0) ON CONFLICT(player_uuid) " +
                "DO UPDATE SET balance = ${if (getBalance(player)-amount < 0) "0" else "balance - $amount"}"

        try {
            val statement: PreparedStatement = connection?.prepareStatement(query) ?: return
            //statement.setInt(1, player.uniqueId.hashCode())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
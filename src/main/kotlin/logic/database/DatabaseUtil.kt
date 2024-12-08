package logic.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseUtil {
    private const val URL = "jdbc:sqlite:database.db"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
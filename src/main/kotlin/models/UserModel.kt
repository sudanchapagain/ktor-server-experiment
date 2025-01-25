package np.com.sudanchapagain.models

import java.sql.Connection
import java.sql.DriverManager
import org.mindrot.jbcrypt.BCrypt

object UserModel {
    private const val DB_URL = "jdbc:sqlite:sqlite.db"

    private fun getConnection(): Connection {
        return DriverManager.getConnection(DB_URL)
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun authenticate(username: String, password: String): Boolean {
        val query = "SELECT * FROM users WHERE username = ?"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, username)
                val resultSet = stmt.executeQuery()

                if (resultSet.next()) {
                    val storedHashedPassword = resultSet.getString("password")
                    return BCrypt.checkpw(password, storedHashedPassword)
                }
            }
        }
        return false
    }

    fun register(username: String, password: String): Boolean {
        val hashedPassword = hashPassword(password)

        val query = "INSERT INTO users (username, password) VALUES (?, ?)"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, username)
                stmt.setString(2, hashedPassword)
                val rowsAffected = stmt.executeUpdate()

                return rowsAffected > 0
            }
        }
    }

    fun isUsernameTaken(username: String): Boolean {
        val query = "SELECT * FROM users WHERE username = ?"
        getConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, username)
                val resultSet = stmt.executeQuery()
                return resultSet.next()
            }
        }
    }
}

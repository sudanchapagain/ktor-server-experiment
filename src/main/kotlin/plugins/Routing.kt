package np.com.sudanchapagain.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import np.com.sudanchapagain.models.UserModel
import np.com.sudanchapagain.models.BalanceModel
import np.com.sudanchapagain.models.UserSession

fun Application.configureRouting() {
    routing {
        get("/") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondText("No authentication. Please log in.")
            } else {
                call.respondText("Authenticated as: ${session.username}")
            }
        }

        post("/auth") {
            val params = call.receiveText().split("&")
                .associate { it.split("=").let { (key, value) -> key to value } }

            val username = params["username"]
            val password = params["password"]

            if (username == null || password == null) {
                call.respondText("Missing username or password.")
                return@post
            }

            if (UserModel.authenticate(username, password)) {
                call.sessions.set(UserSession(id = username, username = username))
                call.respondText("Logged in as $username.")
            } else {
                call.respondText("Invalid credentials.")
            }
        }

        post("/register") {
            val params = call.receiveText().split("&")
                .associate { it.split("=").let { (key, value) -> key to value } }

            val username = params["username"]
            val password = params["password"]

            if (username == null || password == null) {
                call.respondText("Missing username or password.")
                return@post
            }

            if (UserModel.isUsernameTaken(username)) {
                call.respondText("Username already taken.")
                return@post
            }

            val registrationSuccess = UserModel.register(username, password)
            if (registrationSuccess) {
                call.respondText("Registration successful. You can now log in.")
            } else {
                call.respondText("Registration failed.")
            }
        }

        get("/balance") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondText("No authentication. Please log in.")
            } else {
                val balance = BalanceModel.getBalance(session.username)
                call.respondText("Current balance: $balance")
            }
        }

        post("/balance/update") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondText("No authentication. Please log in.")
                return@post
            }

            val params = call.receiveText().split("&")
                .associate { it.split("=").let { (key, value) -> key to value } }

            val amount = params["amount"]?.toIntOrNull()
            val action = params["action"]

            if (amount == null || action !in listOf("add", "subtract")) {
                call.respondText("Invalid input.")
                return@post
            }

            when (action) {
                "add" -> BalanceModel.updateBalance(session.username, amount)
                "subtract" -> BalanceModel.updateBalance(session.username, -amount)
            }
            call.respondText("Balance updated.")
        }
    }
}

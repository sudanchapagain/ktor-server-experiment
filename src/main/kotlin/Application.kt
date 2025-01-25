package np.com.sudanchapagain

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import np.com.sudanchapagain.plugins.configureRouting
import np.com.sudanchapagain.models.UserSession

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            serializer = defaultSessionSerializer()
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600
        }
    }
    configureRouting()
}

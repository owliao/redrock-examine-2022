package cn.udday

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import org.slf4j.event.*
import io.ktor.server.request.*
import io.ktor.serialization.gson.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import cn.udday.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {

        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
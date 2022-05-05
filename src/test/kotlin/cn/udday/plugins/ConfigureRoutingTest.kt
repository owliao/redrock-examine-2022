package cn.udday.plugins;

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class ConfigureRoutingTest {

    @Test
    fun testGet() = testApplication {
        application {

        }
        client.get("/").apply {

        }
    }
}
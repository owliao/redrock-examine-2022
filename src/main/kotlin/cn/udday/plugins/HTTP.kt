package cn.udday.plugins

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
	      allowNonSimpleContentTypes = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
}

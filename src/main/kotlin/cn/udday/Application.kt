package cn.udday

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import cn.udday.plugins.*
import cn.udday.routes.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.*

fun main() {
  TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))
	DataInit.init()
  embeddedServer(Netty, port = 8888) {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    routing{
	    static("/"){
		    defaultResource("static/index.html")
	    }
	    static("/postman") {
		    defaultResource("redrock-examine.json")
	    }
	    manager()
	    user()
	    color()
	    idea()
	    star()
    }
  }.start(wait = true)
}
package cn.udday.utils

import cn.udday.bean.BaseResponse
import cn.udday.tables.ColorThemeDao
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SizedIterable
import java.awt.Color
import kotlin.math.roundToInt


suspend fun ApplicationCall.respondOk(str:String){
	this.respond(BaseResponse(message = str))
}

suspend fun ApplicationCall.respondData(
	data: Any? = mutableMapOf<String, Any?>(),
	message: String = "OK"
){
	this.respond(BaseResponse(message = message,data = data))
}

suspend fun ApplicationCall.respondErr(str:String){
	this.respond(BaseResponse(code = 514,message = str))
}


fun resMap():MutableMap<String, Any?> = LinkedHashMap()

fun <T> SizedIterable<T>.resList(block:(T,MutableMap<String, Any?>)->Unit): List<Any?> =
	buildList {
		this@resList.forEach { data ->
			this.add(resMap().apply {
				block(data,this)
			})
		}
	}


fun <T> SizedIterable<T>.cutPageData(
	page: Int, limit: Int
): PageData<T> {
	val offset = (page.toLong() - 1) * limit
	return PageData(this.limit(limit, offset = offset), offset + limit)
}
data class PageData<T>(val data: SizedIterable<T>, val used: Long)


fun String.toRGB() = listOf(
		Integer.valueOf(this.substring(1,3),16).toInt(),
		Integer.valueOf(this.substring(3,5),16).toInt(),
		Integer.valueOf(this.substring(5),16).toInt(),
	)

fun String.toCymk():List<Int>{
	val red = this.toRGB()[0].toDouble()/255
	val green = this.toRGB()[1].toDouble()/255
	val blue = this.toRGB()[2].toDouble()/255
	val k =1 - Math.max(red,Math.max(green,blue))
	val c = (1 - red - k)/(1-k)
	val m = (1 - green - k)/(1-k)
	val y = (1 - blue - k)/(1-k)
	return listOf((c * 100).roundToInt(),(m * 100).roundToInt(),(y * 100).roundToInt(),(k * 100).roundToInt())
}

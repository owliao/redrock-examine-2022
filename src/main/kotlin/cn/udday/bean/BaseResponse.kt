package cn.udday.bean

open class BaseResponse(
	val code: Int = 114,
	val message: String = "OK",
	val data: Any? = mutableMapOf<String, Any?>()) {

}
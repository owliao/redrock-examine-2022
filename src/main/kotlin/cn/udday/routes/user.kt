package cn.udday.routes

import cn.udday.bean.BaseResponse
import cn.udday.tables.*
import cn.udday.tables.ColorAndThemeDao.Companion.findById
import cn.udday.utils.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Routing.user(){
	route("/user"){
		//注册
		post("/register"){
			val receiveParameters = call.receiveParameters()
      var phoneNumber = receiveParameters["phone_number"]?.toLongOrNull() ?: return@post
      val name = receiveParameters["name"] ?: return@post
			newSuspendedTransaction {
				when(UserDao.find { UserTable.phoneNumber eq phoneNumber }.count()){
					0L ->{
						UserDao.new {
							this.phoneNumber = phoneNumber
							this.name = name
						}
						call.respondOk("成功注册")
					}
					else ->{
						call.respondErr("账号已存在")
					}
				}
			}
		}

		//登陆
		post("/login"){
			val receiveParameters = call.receiveParameters()
			var phoneNumber = receiveParameters["phone_number"]?.toLongOrNull() ?: return@post
			newSuspendedTransaction {
				val user = UserDao.find { UserTable.phoneNumber eq phoneNumber }.firstOrNull()
				if (user != null){
					val token = Auth.makeToken(user.id.value)
					val refreshToken = Auth.makeToken(user.id.value, true)
					call.respondData(resMap().apply {
						put("token",token)
						put("refreshToken",refreshToken)
					})
				}else{
					call.respondErr("账号不存在")
				}
			}
		}

		//无限时长的token
		post("/long_login"){
			val receiveParameters = call.receiveParameters()
			var phoneNumber = receiveParameters["phone_number"]?.toLongOrNull() ?: return@post
			newSuspendedTransaction {
				val user = UserDao.find { UserTable.phoneNumber eq phoneNumber }.firstOrNull()
				if (user != null){
					val token = Auth.makeLongToken(user.id.value)
					call.respondData(resMap().apply {
						put("token",token)
					})
				}else{
					call.respondErr("账号不存在")
				}
			}
		}
	}
}
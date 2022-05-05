package cn.udday.routes

import cn.udday.tables.*
import cn.udday.utils.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Routing.star(){
	authenticate(){
		route("star"){
			//收藏
			post("/star") {
				val receiveParameters = call.receiveParameters()
				val shadeId = receiveParameters["shade_id"]?.toLongOrNull()
				val name = receiveParameters["name"]?:"颜色"
				newSuspendedTransaction {
					val user = call.getUser() ?: UserDao.findById(1)!!
					shadeId ?: run {
						call.respondErr("shade_id不能为空")
						return@newSuspendedTransaction
					}
					val colorShade = ColorShadeDao.find { ColorShadeTable.id eq shadeId }.firstOrNull()
					colorShade ?: run {
						call.respondErr("未找到渐变色")
						return@newSuspendedTransaction
					}
					StarDao.new {
						this.name = name
						this.user = user
						this.colorShade = colorShade
					}
					call.respondOk("收藏成功")
				}
			}

			//删除收藏
			post("delete_star"){
				val receiveParameters = call.receiveParameters()
				val starId = receiveParameters["star_id"]?.toLongOrNull()
				newSuspendedTransaction {
					val user = call.getUser() ?: UserDao.findById(1)!!
					starId ?: run {
						call.respondErr("star_id不能为空")
						return@newSuspendedTransaction
					}
					val star = StarDao.find { StarTable.id eq starId }.firstOrNull()
					star ?: run {
						call.respondErr("未找到收藏")
						return@newSuspendedTransaction
					}
					star.delete()
					call.respondOk("删除成功")
				}
			}

			//获得收藏
			get("/star_list"){
				val query = call.request.queryParameters
				val page = query["page"]?.toIntOrNull() ?: 1
				val limit = query["limit"]?.toIntOrNull() ?: 10
				newSuspendedTransaction {
					val user = call.getUser()?: ColorAndThemeDao.findById(1L)!!
					val stars = StarDao.find { StarTable.userId eq user.id }
					val (nowStarts,used) = stars.cutPageData(page,limit)
					val datas = nowStarts.resList { starDao, mutableMap ->
							mutableMap.apply {
								put(starDao::id.name,starDao.id.value)
								put(starDao::name.name,starDao.name)
								put(starDao::colorShade.name,starDao.colorShade.toMap())
							}
					}
					val message = resMap().apply {
						put("has_more",stars.count() >= used)
						put("star_list",datas)
					}
					call.respondData(message)
				}
			}

		}
	}
}
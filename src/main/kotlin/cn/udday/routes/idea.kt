package cn.udday.routes

import cn.udday.tables.*
import cn.udday.utils.resList
import cn.udday.utils.resMap
import cn.udday.utils.respondData
import cn.udday.utils.respondErr
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Routing.idea(){

	route("idea"){

		//返回首页信息
		get("/idea"){
			newSuspendedTransaction {
				call.respondData(IdeaDao.all().resList { ideaDao, mutableMap ->
					mutableMap.apply {
						put(ideaDao::id.name,ideaDao.id.value)
						put(ideaDao::name.name,ideaDao.name)
						put(ideaDao::image.name,ideaDao.image.imageLink)
					}
				})
			}
		}

		//返回list
		get("/idea_detail_list") {
			val query = call.request.queryParameters
			val ideaId = query["idea_id"]?.toLongOrNull()
			newSuspendedTransaction {
				ideaId?:run {
					call.respondErr("idea_id参数不能为空")
					return@newSuspendedTransaction
				}
				val details = IdeaAndDetailDao.find { IdeaAndDetailTable.idea eq ideaId }
				if (details.empty()){
					call.respondErr("没有详细页")
					return@newSuspendedTransaction
				}
				call.respondData(details.resList { ideaAndDetailDao, mutableMap ->
					mutableMap.apply {
						put(ideaAndDetailDao::ideaDetail.name,ideaAndDetailDao.ideaDetail.id.value)
					}
				})
			}
		}

		//返回详细信息
		get("/idea_detail"){
			val query = call.request.queryParameters
				val ideaDetailId = query["idea_detail_id"]?.toLongOrNull()
			newSuspendedTransaction {
				ideaDetailId ?: run {
					call.respondErr("idea_detail不能为空")
					return@newSuspendedTransaction
				}
				val ideaDetail = IdeaDetailDao.find{ IdeaDetailTable.id eq ideaDetailId }.firstOrNull()
				ideaDetail ?: run {
					call.respondErr("不存在次灵感详细")
					return@newSuspendedTransaction
				}
				val colors = resMap().apply {
					put("color_1",ideaDetail.colorDetail.colorId1.toMap())
					put("color_2",ideaDetail.colorDetail.colorId2.toMap())
					put("color_3",ideaDetail.colorDetail.colorId3.toMap())
					put("color_4",ideaDetail.colorDetail.colorId4.toMap())
					put("color_5",ideaDetail.colorDetail.colorId5.toMap())
					put("color_6",ideaDetail.colorDetail.colorId6.toMap())
					put("color_7",ideaDetail.colorDetail.colorId7.toMap())
				}
				val shades = resMap().apply {
					put("shade_list", buildList {
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade1::id.name, ideaDetail.colorDetail.shade1.id.value)
							put("shade", ideaDetail.colorDetail.shade1.toMap())
						})
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade2::id.name, ideaDetail.colorDetail.shade2.id.value)
							put("shade", ideaDetail.colorDetail.shade2.toMap())
						})
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade3::id.name, ideaDetail.colorDetail.shade3.id.value)
							put("shade", ideaDetail.colorDetail.shade3.toMap())
						})
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade4::id.name, ideaDetail.colorDetail.shade4.id.value)
							put("shade", ideaDetail.colorDetail.shade4.toMap())
						})
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade5::id.name, ideaDetail.colorDetail.shade5.id.value)
							put("shade", ideaDetail.colorDetail.shade5.toMap())
						})
						add(mutableMapOf<String, Any?>().apply {
							put(ideaDetail.colorDetail.shade6::id.name, ideaDetail.colorDetail.shade6.id.value)
							put("shade", ideaDetail.colorDetail.shade6.toMap())
						})
					})
				}
				val data = resMap().apply {
					put(ideaDetail::title.name,ideaDetail.title)
					put(ideaDetail::image.name,ideaDetail.image.imageLink)
					put(ideaDetail::intro.name,ideaDetail.intro)
					put("colors",colors)
					put("shades",shades)
				}
				call.respondData(data)
			}
		}

	}
}




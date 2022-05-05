package cn.udday.routes

import cn.udday.tables.*
import cn.udday.tables.ColorAndThemeDao.Companion.findById
import cn.udday.utils.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Routing.color(){
		route("color"){
			//获得首页颜色pageId
			get("/page"){
				newSuspendedTransaction {
					val themes = ColorThemeDao.all()
					if (themes.count() == 0L){
						call.respondErr("没有颜色")
						return@newSuspendedTransaction
					}

					call.respondData(
						resMap().apply {
						put("count",themes.count())
						put("list",themes.resList{ data,map->
							map.apply {
								put(ColorThemeDao::id.name,data.id.value)
								put(ColorThemeDao::theme.name,data.theme)
							}
						})
					}
					)
				}
			}

			//获得每页的颜色list
			get("/color_list"){
				val query = call.request.queryParameters
				val colorThemeId = query["theme_id"]?.toLongOrNull()
				val page = query["page"]?.toIntOrNull() ?: 1
				val limit = query["limit"]?.toIntOrNull() ?: 10
				newSuspendedTransaction {
					colorThemeId ?: run {
						call.respondErr("theme_id不能为空")
						return@newSuspendedTransaction
					}
					val allColors = ColorAndThemeDao.find{ ColorAndThemeTable.theme eq colorThemeId }
					val (colors,used) = allColors.cutPageData(page,limit)
					if (allColors.count() == 0L){
						call.respondErr("没有颜色")
						return@newSuspendedTransaction
					}
					call.respondData(resMap().apply {
						put("has_more",allColors.count() > used)
						put("color_list",colors.resList { colorThemeDao, mutableMap ->
							mutableMap.putAll(colorThemeDao.color.toMap())
						})
					})
				}
			}

			//获得颜色详细页
			get("/color_detail"){
				val query = call.request.queryParameters
				val colorDetailId = query["color_detail_id"]?.toLongOrNull()
				newSuspendedTransaction {
					colorDetailId ?: run {
						call.respondErr("color_detail_id不能为空")
						return@newSuspendedTransaction
					}
					val colorDetail = ColorDetailDao.find { ColorDetailTable.colorId1 eq colorDetailId }.firstOrNull() ?: run{
						call.respondErr("这个颜色没有详情页")
						return@newSuspendedTransaction
					}
					val colors = resMap().apply {
						put("color_1",colorDetail.colorId1.toMap())
						put("color_2",colorDetail.colorId2.toMap())
						put("color_3",colorDetail.colorId3.toMap())
						put("color_4",colorDetail.colorId4.toMap())
						put("color_5",colorDetail.colorId5.toMap())
						put("color_6",colorDetail.colorId6.toMap())
						put("color_7",colorDetail.colorId7.toMap())
					}
					val shades = resMap().apply {
						put("shade_list", buildList{
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade1::id.name, colorDetail.shade1.id.value)
								put("shade", colorDetail.shade1.toMap())
							})
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade2::id.name, colorDetail.shade2.id.value)
								put("shade", colorDetail.shade2.toMap())
							})
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade3::id.name, colorDetail.shade3.id.value)
								put("shade", colorDetail.shade3.toMap())
							})
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade4::id.name,colorDetail.shade4.id.value)
								put("shade", colorDetail.shade4.toMap())
							})
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade5::id.name,colorDetail.shade5.id.value)
								put("shade",colorDetail.shade5.toMap())
							})
							add(mutableMapOf<String, Any?>().apply {
								put(colorDetail.shade6::id.name,colorDetail.shade6.id.value)
								put("shade",colorDetail.shade6.toMap())
							})
						})
					}
					call.respondData(resMap().apply {
						put("intro",colorDetail.colorId1.intro)
						put("colors",colors)
						put("shades",shades)
					})
				}
			}

		}
}
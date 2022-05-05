package cn.udday.tables

import cn.udday.tables.ColorDao.Companion.referrersOn
import cn.udday.tables.ColorShadeDao.Companion.referrersOn
import cn.udday.tables.ColorTable.uniqueIndex
import cn.udday.utils.resMap
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SizedIterable

object ColorTable : LongIdTable("color"){
	val name = varchar("name",20).uniqueIndex()
	val hex = varchar("hex",6)

	val r = integer("r")
	val g = integer("g")
	val b = integer("b")

	val c = integer("c")
	val m = integer("m")
	val k = integer("k")
	val y = integer("y")
	//简介
	val intro = varchar("intro",200).default("暂无介绍")
}

class ColorDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ColorDao>(ColorTable)

	var name by ColorTable.name
	var hex by ColorTable.hex

	var r by ColorTable.r
	var g by ColorTable.g
	var b by ColorTable.b

	var c by ColorTable.c
	var m by ColorTable.m
	var k by ColorTable.k
	var y by ColorTable.y

	var intro by ColorTable.intro
}

fun ColorDao.toMap() =
	resMap().apply {
	put(ColorTable::id.name,this@toMap.id.value)
	put(ColorTable::name.name,this@toMap.name)
	put(ColorTable::hex.name,this@toMap.hex)
	put(ColorTable::r.name,this@toMap.r)
	put(ColorTable::g.name,this@toMap.g)
	put(ColorTable::b.name,this@toMap.b)
	put(ColorTable::c.name,this@toMap.c)
	put(ColorTable::m.name,this@toMap.m)
	put(ColorTable::k.name,this@toMap.k)
	put(ColorTable::y.name,this@toMap.y)
}


object ColorShadeTable:LongIdTable("color_shade"){
	val shade1 = reference("color_id_1", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val shade2 = reference("color_id_2", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val shade3 = reference("color_id_3", ColorTable.id,onDelete = ReferenceOption.CASCADE).nullable().default(null)
	val shade4 = reference("color_id_4", ColorTable.id,onDelete = ReferenceOption.CASCADE).nullable().default(null)
	val shade5 = reference("color_id_5", ColorTable.id,onDelete = ReferenceOption.CASCADE).nullable().default(null)
	val shade6 = reference("color_id_6", ColorTable.id,onDelete = ReferenceOption.CASCADE).nullable().default(null)
	val shade7 = reference("color_id_7", ColorTable.id,onDelete = ReferenceOption.CASCADE).nullable().default(null)
}

class ColorShadeDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ColorShadeDao>(ColorShadeTable)

	var shade1 by ColorDao referencedOn ColorShadeTable.shade1
	var shade2 by ColorDao referencedOn ColorShadeTable.shade2
	var shade3 by ColorDao optionalReferencedOn ColorShadeTable.shade3
	var shade4 by ColorDao optionalReferencedOn ColorShadeTable.shade4
	var shade5 by ColorDao optionalReferencedOn ColorShadeTable.shade5
	var shade6 by ColorDao optionalReferencedOn ColorShadeTable.shade6
	var shade7 by ColorDao optionalReferencedOn ColorShadeTable.shade7
}

fun ColorShadeDao.toMap() = buildList {
	add(mutableMapOf<String,Any?>().apply {put("color",shade1.toMap())})
	add(mutableMapOf<String,Any?>().apply {put("color",shade2.toMap())})
	shade3?.let { add(mutableMapOf<String,Any?>().apply {put("color",it.toMap())}) }
	shade4?.let { add(mutableMapOf<String,Any?>().apply {put("color",it.toMap())}) }
	shade5?.let { add(mutableMapOf<String,Any?>().apply {put("color",it.toMap())}) }
	shade6?.let { add(mutableMapOf<String,Any?>().apply {put("color",it.toMap())}) }
	shade7?.let { add(mutableMapOf<String,Any?>().apply {put("color",it.toMap())}) }
}

object ColorDetailTable:LongIdTable("color_detail"){
	val colorId1 = reference("color_id_1", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId2 = reference("color_id_2", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId3 = reference("color_id_3", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId4 = reference("color_id_4", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId5 = reference("color_id_5", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId6 = reference("color_id_6", ColorTable.id,onDelete = ReferenceOption.CASCADE)
	val colorId7 = reference("color_id_7", ColorTable.id,onDelete = ReferenceOption.CASCADE)

	val shade1 = reference("shade1",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
	val shade2 = reference("shade2",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
	val shade3 = reference("shade3",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
	val shade4 = reference("shade4",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
	val shade5 = reference("shade5",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
	val shade6 = reference("shade6",ColorShadeTable.id, onDelete = ReferenceOption.CASCADE)
}

class ColorDetailDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ColorDetailDao>(ColorDetailTable)

	var colorId1 by ColorDao referencedOn ColorDetailTable.colorId1
	var colorId2 by ColorDao referencedOn ColorDetailTable.colorId2
	var colorId3 by ColorDao referencedOn ColorDetailTable.colorId3
	var colorId4 by ColorDao referencedOn ColorDetailTable.colorId4
	var colorId5 by ColorDao referencedOn ColorDetailTable.colorId5
	var colorId6 by ColorDao referencedOn ColorDetailTable.colorId6
	var colorId7 by ColorDao referencedOn ColorDetailTable.colorId7
	var shade1 by ColorShadeDao referencedOn ColorDetailTable.shade1
	var shade2 by ColorShadeDao referencedOn ColorDetailTable.shade2
	var shade3 by ColorShadeDao referencedOn ColorDetailTable.shade3
	var shade4 by ColorShadeDao referencedOn ColorDetailTable.shade4
	var shade5 by ColorShadeDao referencedOn ColorDetailTable.shade5
	var shade6 by ColorShadeDao referencedOn ColorDetailTable.shade6
}



object ColorThemeTable : LongIdTable("color_theme"){
	val theme = varchar("theme_name",20)
	val isShow = bool("show").default(true)
}

class ColorThemeDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ColorThemeDao>(ColorThemeTable)
	var theme by ColorThemeTable.theme
	var isShow by ColorThemeTable.isShow
}

object ColorAndThemeTable : LongIdTable("color_and_theme"){
	val theme = reference("color_theme_id",ColorThemeTable.id,onDelete = ReferenceOption.CASCADE)
	val color = reference("color_id",ColorTable.id,onDelete = ReferenceOption.CASCADE)
}

class ColorAndThemeDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ColorAndThemeDao>(ColorAndThemeTable)
	var theme by ColorThemeDao referencedOn ColorAndThemeTable.theme
	var color by ColorDao referencedOn ColorAndThemeTable.color
}
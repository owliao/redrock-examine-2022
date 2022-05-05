package cn.udday.tables

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object StarTable:LongIdTable("star") {
	val name = varchar("name",20)
	val userId = reference("user_id",UserTable.id,onDelete = ReferenceOption.CASCADE)
	val ColorShadeId = reference("color_shade_id",ColorShadeTable.id,onDelete = ReferenceOption.CASCADE)
}

class StarDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<StarDao>(StarTable)

	var name by StarTable.name
	var user by UserDao referencedOn StarTable.userId
	var colorShade by ColorShadeDao referencedOn StarTable.ColorShadeId
}
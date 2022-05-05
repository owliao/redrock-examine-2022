package cn.udday.tables

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption



object IdeaTable:LongIdTable("idea"){
	val name = varchar("name",20)
	val image = reference("image", ImageTable.id,onDelete = ReferenceOption.CASCADE)
}

class IdeaDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<IdeaDao>(IdeaTable)

	val name by IdeaTable.name
	var image by ImageDao referencedOn IdeaTable.image
}

object IdeaDetailTable :LongIdTable("idea_detail"){
	val title = varchar("title",20)
	val image = reference("image", ImageTable.id,onDelete = ReferenceOption.CASCADE)
	val intro = varchar("intro",200)
	val colorDetail = reference("color_detail", ColorDetailTable.id,onDelete = ReferenceOption.CASCADE)
}

class IdeaDetailDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<IdeaDetailDao>(IdeaDetailTable)
	val title by IdeaDetailTable.title
	val intro by IdeaDetailTable.intro
	var image by ImageDao referencedOn IdeaDetailTable.image
	var colorDetail by ColorDetailDao referencedOn IdeaDetailTable.colorDetail
}

object IdeaAndDetailTable :LongIdTable("idea_and_detail"){
	val idea = reference("color_idea_id",IdeaTable.id,onDelete = ReferenceOption.CASCADE)
	val colorDetail = reference("idea_detail_id",IdeaDetailTable.id,onDelete = ReferenceOption.CASCADE)
}

class IdeaAndDetailDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<IdeaAndDetailDao>(IdeaAndDetailTable)
	var idea by IdeaDao referencedOn IdeaAndDetailTable.idea
	var ideaDetail by IdeaDetailDao referencedOn IdeaAndDetailTable.colorDetail
}


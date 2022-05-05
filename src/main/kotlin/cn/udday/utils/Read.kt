package cn.udday.utils

import cn.udday.DataInit
import cn.udday.tables.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

suspend fun main(){
	DataInit.init()
	newSuspendedTransaction {
		val colorTheme = ColorThemeDao.find { ColorThemeTable.id eq 7L }.firstOrNull()!!
		val allColors = ColorAndThemeDao.find{ ColorAndThemeTable.theme eq colorTheme.id }.toList()
		for (i in allColors.indices){
				val myShade1 = getColorShadow(i,allColors)
				val myShade2 = getColorShadow(i+1,allColors)
				val myShade3 = getColorShadow(i+2,allColors)
				val myShade4 = getColorShadow(i+3,allColors)
				val myShade5 = getColorShadow(i+4,allColors)
				val myShade6 = getColorShadow(i+5,allColors)
				ColorDetailDao.new {
					colorId1 = allColors[getItem(i,allColors.size)].color
					colorId2 = allColors[getItem(i+1,allColors.size)].color
					colorId3 = allColors[getItem(i+2,allColors.size)].color
					colorId4 = allColors[getItem(i+3,allColors.size)].color
					colorId5 = allColors[getItem(i+4,allColors.size)].color
					colorId6 = allColors[getItem(i+5,allColors.size)].color
					colorId7 = allColors[getItem(i+6,allColors.size)].color
					shade1 = myShade1
					shade2 = myShade2
					shade3 = myShade3
					shade4 = myShade4
					shade5 = myShade5
					shade6 = myShade6
				}
			println("循环完成$i")
		}
	}
}
fun getColorShadow(i:Int,allColors:List<ColorAndThemeDao>):ColorShadeDao{
	return when(i%5){
		0 -> {
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
			}
		}
		1 ->{
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
				shade3 = allColors[getItem(i+2,allColors.size)].color
			}
		}
		2 ->{
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
				shade3 = allColors[getItem(i+2,allColors.size)].color
			}
		}
		3 ->{
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
				shade3 = allColors[getItem(i+2,allColors.size)].color
				shade4 = allColors[getItem(i+3,allColors.size)].color
			}
		}
		4 ->{
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
				shade3 = allColors[getItem(i+2,allColors.size)].color
				shade4 = allColors[getItem(i+3,allColors.size)].color
				shade5 = allColors[getItem(i+4,allColors.size)].color
			}
		}
		else ->{
			ColorShadeDao.new {
				shade1 = allColors[getItem(i,allColors.size)].color
				shade2 = allColors[getItem(i+1,allColors.size)].color
			}
		}
	}
}

fun getItem(i:Int,all:Int):Int{
	var res = i
	if(res>=all-1){
		res -= all-1
	}
	if(res>=all-1){
		res -= all-1
	}
	return res
}

suspend fun readColor(){
	DataInit.init()
	newSuspendedTransaction {
		val file = File("src/main/resources/color")
		val all = file.readLines()
		for(i in 0..31){
			if(all[i].substring(0,1) == "#") continue
			val rgb = all[i+4].toRGB()
			val cymk = all[i+4].toCymk()
			var color = ColorDao.new {
				name = all[i]
				hex = all[i+4].substring(1)
				r = rgb[0]
				g = rgb[1]
				b = rgb[2]

				c = cymk[0]
				y = cymk[1]
				m = cymk[2]
				k = cymk[3]
			}
			val theme = ColorThemeDao.new {
				theme = "立夏"
			}
			ColorAndThemeDao.new {
				this.theme = theme
				this.color =color
			}
		}
	}
}
package com.xazhuxj.smartroute

data class Route(
    var dirJdStartAlpha: Boolean, //交点-起点-方向角 or 交点-起点-终点
    var kJD:Double,
    var xJD:Double,
    var yJD:Double,
    var xStart:Double,
    var yStart:Double,
    var xEnd:Double,
    var yEnd:Double,
    var alpha:Double,
    var r0:Double,
    var l0:Double,
    var kNo:Double,
    var length:Double
)

package com.xazhuxj.smartroute.models

import androidx.annotation.Px
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.text.*

//kotlin 静态类的写法， 部分静态方法采用伴随对象方式实现 companion object
//object SurMath {
//    /**
//     * 度分秒角度提取度、分、秒
//     * 101 02 20.1 => 101.02201  -> 101 02 20.1
//     * 1 40 00 => 1.4  -> 1 40 00
//     */
//    fun dmsToDMS(dmsAngle: Double): Triple<Int, Int, Double> {
//        val dms = dmsAngle * 10000
//        var angle = dms.toInt()
//        val d: Int = angle / 10000
//        angle -= d * 10000
//        val m: Int = angle / 100
//        val s = dms - d * 10000 - m * 100
//
//        return Triple(d, m, s)
//    }
//}


/**
 * 度分秒角度提取度、分、秒
 * 101 02 20.1 => 101.02201  -> 101 02 20.1
 * 1 40 00 => 1.4  -> 1 40 00
 */
fun dmsToDMS(dmsAngle: Double): Triple<Int, Int, Double> {
    val dms = dmsAngle * 10000
    var angle = dms.toInt()
    val d: Int = angle / 10000
    angle -= d * 10000
    val m: Int = angle / 100
    val s = dms - d * 10000 - m * 100

    return Triple(d, m, s)
}

/**
 * 将度分秒角度值直接转换为弧度值
 */
fun dmsToRadian(dmsAngle: Double): Double {
    val dms = dmsToDMS(dmsAngle)
    return (dms.first + dms.second / 60.0 + dms.third / 3600.0)/180.0*PI
}

/**
 * 先将弧度转换为秒，再提取相应的度分秒值
 */
fun radianToDMS(radAngle: Double): Triple<Int, Int, Double> {
    val f = if (radAngle >=0)  1 else -1

    val rad = radAngle * 180.0 * 3600.0 / Math.PI + f * 0.00001
    var angle = rad.toInt()
    val d = angle / 3600
    angle -= d * 3600
    val m = angle / 60
    val s = rad - d * 3600 - m * 60 - f*0.00001
    return Triple(d, m, s)
}


/**
 * 将弧度转换为度分秒的组合值
 *
 * @param radAngle 以弧度为单位的角度值
 * @return 返回双精度类型的度分秒
 */
fun radianToDms(radAngle: Double): Double {
    val dms = radianToDMS(radAngle)
    return dms.first + dms.second / 100.0 + dms.third / 10000.0
}

fun radianToDmsString(radAngle: Double): String {
    val f = if(radAngle >= 0) 1 else -1
    val dms = radianToDMS(radAngle)
    return "${if(f==1) "" else "-1"}${f*dms.first}°${f*dms.second}′${String.format("%.5f", f*dms.third)}″"
}

/**
 * 计算A->B的坐标方位角与距离
 *
 * @param xA A点的x坐标
 * @param yA A点的y坐标
 * @param xB B点的x坐标
 * @param yB B点的y坐标
 * @return 以弧度为单位的A->B的坐标方位角
 */
fun azimuth(xA: Double, yA: Double, xB : Double, yB:Double ) : Pair<Double, Double>{
    val dx = xB - xA
    val dy = yB - yA

    val a = atan2(dy, dx) + (if(dy>=0) 0 else 1)*2* PI
    val d = sqrt(dx * dx + dy *dy)
    return Pair(a, d)
}

/**
 * 计算测量坐标系下点的平移、旋转、伸缩
 *
 * @param dx x方向的平移量
 * @param dy y方向的平移量
 * @param alpha   α是旧网 x′轴 逆转 至新网 x轴的转角
 * @param k 伸缩比
 * @param xp p点x
 * @param yp p点y
 * @return 新的坐标(x, y)
 */
fun transformXY(dx:Double, dy:Double, alpha:Double, k:Double, xp: Double, yp:Double): Pair<Double, Double> {
    val x = dx + k * (xp * cos(alpha) - yp * sin(alpha))
    val y = dy + k * (xp * sin(alpha) + yp * cos(alpha))

    return Pair(x, y)
}
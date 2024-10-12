package com.xazhuxj.smartroute.models

import kotlin.math.*

/**
 * 圆曲线
 *
 * @property jd 圆曲线交点
 * @property radius 圆曲线半径
 */
class CircleCurve private constructor(jd: Point, radius: Double): Curve(jd, radius) {
    /**
     * 切线长
     */
    override val T
        get() = radius * tan(alpha * 0.5)

    /**
     * 曲线长
     */
    override val L
        get() = radius * alpha

    /**
     * 外矢距
     */
    override val E
        get() = radius * (1 / cos(alpha * 0.5) - 1)


    val zy = Point(note ="ZY" )
    val qz = Point(note ="QZ" )
    val yz = Point(note ="YZ" )


    init {
        jd.note="JD"
    }

    constructor(start: Point, jd: Point, radius:Double, alpha : Double) : this(jd, radius) {
        flag = if (alpha >= 0.0) 1 else -1
        this.alpha = flag * dmsToRadian(alpha)

        //计算偏转角
        val a12 = azimuth(start, jd)
        alpha0 = a12

        var a23 = a12 + this.alpha * flag
        if(a23 < 0) a23 += 2*PI
        if(a23 >= 2 * PI) a23 -= 2*PI

        with(zy){
            kNo = jd.kNo - T
            x = jd.x - T * cos(a12)
            y = jd.y - T * sin(a12)
            note = "ZY"
        }

        qz.kNo = zy.kNo + L * 0.5
        yz.kNo = zy.kNo + L

        with(qz){
            calPointInCurve(this) //避免上面算法的范围引用
            note = "QZ"
        }

        with(yz){
            x = jd.x + T * cos(a23)
            y = jd.y + T * sin(a23)
            note = "YZ"
        }
    }


    override fun toString(): String {
        return  "$jd" +
                "$zy" +
                "$qz" +
                "$yz" +
                "R=$radius\n" +
                "α=${radianToDmsString(alpha)},${if(flag==1) "右偏" else "左偏"}\n" +
                "T=$T\n" +
                "L=$L\n" +
                "E=$E\n" +
                "q=$q\n" +
                "α0=${radianToDmsString(alpha0)}\n"
    }


    /**
     * 计算圆曲线上点的坐标
     *
     * @param pt 计算点
     */
    private fun calPointInCurve(pt: Point) {
        val alphai = (pt.kNo - zy.kNo) / radius

        with(pt){
            x = radius * sin(alphai)
            y = flag * radius * (1 - cos(alphai))
            transformXY(zy, alpha0, pt)
        }
    }


    /**
     * 根据里程桩号计算圆曲线上的坐标
     *
     * @param kno 里程桩号
     * @return 点坐标
     */
    override fun calPointOnCurveByKno(kno: Double): Point? {
        if (kno < zy.kNo || kno > yz.kNo) return null //不是圆曲线上有效范围

        if (abs(kno - zy.kNo)<=0.01 ) return zy
        if (abs(kno - qz.kNo)<=0.01 ) return qz
        if (abs(kno - yz.kNo)<=0.01 ) return yz

        return Point(kNo = kno).also(::calPointInCurve)
    }


    override fun calAllPoints(length: Double): ArrayList<Point>{
        val points = ArrayList<Point>()

        points.add(zy)

        //ZY --> QZ
        var kno = zy.kNo
        while (kno + length < qz.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(qz)

        //QZ --> YZ
        kno = qz.kNo
        while (kno + length < yz.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(yz)

        return points
    }
}
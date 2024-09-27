package com.xazhuxj.smartroute.models

import kotlin.math.*

/**
 * 圆曲线
 *
 * @property JD 圆曲线交点
 * @property R 圆曲线半径
 */
class CircleCurve private constructor(JD: Point, radius: Double): Curve(JD, radius) {
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


    val ZY = Point(note ="ZY" )
    val QZ = Point(note ="QZ" )
    val YZ = Point(note ="YZ" )


    init {
        JD.note="JD"
    }

    constructor(start: Point, JD: Point, radius:Double, alpha : Double) : this(JD, radius) {
        flag = if (alpha >= 0.0) 1 else -1
        this.alpha = flag * dmsToRadian(alpha)

        //计算偏转角
        val a12 = azimuth(start, JD)
        alpha0 = a12

        var a23 = a12 + this.alpha * flag
        if(a23 < 0) a23 += 2*PI
        if(a23 >= 2 * PI) a23 -= 2*PI

        with(ZY){
            kNo = JD.kNo - T
            x = JD.x - T * cos(a12)
            y = JD.y - T * sin(a12)
            note = "ZY"
        }

        QZ.kNo = ZY.kNo + L * 0.5
        YZ.kNo = ZY.kNo + L

        with(QZ){
            calPointInCurve(this) //避免上面算法的范围引用
            note = "QZ"
        }

        with(YZ){
            x = JD.x + T * cos(a23)
            y = JD.y + T * sin(a23)
            note = "YZ"
        }
    }


    override fun toString(): String {
        return  "$JD" +
                "$ZY" +
                "$QZ" +
                "$YZ" +
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
//        if (pt.kNo < ZY.kNo || pt.kNo > YZ.kNo)
//            throw RangeException(-1, "计算点的里程桩号:${pt.kNo} 不在该圆曲线的范围内:${ZY.kNo}-${YZ.kNo}")

        val alphai = (pt.kNo - ZY.kNo) / radius

        with(pt){
            x = radius * sin(alphai)
            y = flag * radius * (1 - cos(alphai))
            transformXY(ZY, alpha0, pt)
        }
    }


    /**
     * 根据里程桩号计算圆曲线上的坐标
     *
     * @param kno 里程桩号
     * @return 点坐标
     */
    override fun calPointOnCurveByKno(kno: Double): Point? {
        if (kno < ZY.kNo || kno > YZ.kNo) return null //不是圆曲线上有效范围

        if (abs(kno - ZY.kNo)<=0.01 ) return ZY
        if (abs(kno - QZ.kNo)<=0.01 ) return QZ
        if (abs(kno - YZ.kNo)<=0.01 ) return YZ

        return Point(kNo = kno).also(::calPointInCurve)
    }


    override fun calAllPoints(length: Double): MutableList<Point>{
        val points: MutableList<Point> = ArrayList<Point>()

        points.add(ZY)

        //ZY --> QZ
        var kno = ZY.kNo
        while (kno + length < QZ.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(QZ)

        //QZ --> YZ
        kno = QZ.kNo
        while (kno + length < YZ.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(YZ)

        return points
    }
}
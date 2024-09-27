package com.xazhuxj.smartroute.models

//import org.w3c.dom.ranges.RangeException
import kotlin.math.*

/**
 * 圆曲线
 *
 * @property jd 圆曲线交点
 * @property R 圆曲线半径
 */
class CircleCurve private constructor(jd: GPoint, radius: Double): Curve(jd, radius) {
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


    val zy = GPoint(note ="ZY" )
    val qz = GPoint(note ="QZ" )
    val yz = GPoint(note ="YZ" )


    init {
        jd.note="JD"
    }


    constructor(start: GPoint, jd: GPoint, end: GPoint, radius:Double) : this(jd, radius) {
        //判断 start -> JD -> end 是偏右？ 还是 偏左？
        flag = isRight(start, jd, end)

        //计算偏转角
        val a12 = azimuth(start, jd)
        alpha0 = a12
        val a23 = azimuth(jd, end)
        if(flag == 1) {
            alpha = a23 - a12
        }
        else {
            alpha = a12 - a23
        }
        if(alpha <0) alpha += 2*PI

        with(zy){ //按大地测量坐标计算出zy点坐标
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

        with(yz){ //按大地测量坐标计算出yz点坐标
            x = jd.x + T * cos(a23)
            y = jd.y + T * sin(a23)
            note = "YZ"
        }
    }

    constructor(start: GPoint, JD: GPoint, radius:Double, alpha : Double) : this(JD, radius) {
        flag = if (alpha >= 0.0) 1 else -1
        this.alpha = flag * dmsToRadian(alpha)

        //计算偏转角
        val a12 = azimuth(start, JD)
        alpha0 = a12

        var a23 = a12 + this.alpha * flag
        if(a23 < 0) a23 += 2*PI
        if(a23 >= 2 * PI) a23 -= 2*PI

        with(zy){
            kNo = JD.kNo - T
            x = JD.x - T * cos(a12)
            y = JD.y - T * sin(a12)
            note = "ZY"
        }

        qz.kNo = zy.kNo + L * 0.5
        yz.kNo = zy.kNo + L

        with(qz){
            calPointInCurve(this) //避免上面算法的范围引用
            note = "QZ"
        }

        with(yz){
            x = JD.x + T * cos(a23)
            y = JD.y + T * sin(a23)
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
    private fun calPointInCurve(pt: GPoint) {
//        if (pt.kNo < ZY.kNo || pt.kNo > YZ.kNo)
//            throw RangeException(-1, "计算点的里程桩号:${pt.kNo} 不在该圆曲线的范围内:${ZY.kNo}-${YZ.kNo}")

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
    override fun calPointOnCurveByKno(kno: Double): GPoint? {
        if (kno < zy.kNo || kno > yz.kNo) return null //不是圆曲线上有效范围

        if (abs(kno - zy.kNo)<=0.01 ) return zy
        if (abs(kno - qz.kNo)<=0.01 ) return qz
        if (abs(kno - yz.kNo)<=0.01 ) return yz

        return GPoint(kNo = kno).also(::calPointInCurve)
    }


    override fun calAllPoints(length: Double): ArrayList<GPoint>{
        val points = ArrayList<GPoint>()

        points.add(zy)

        //ZY --> QZ
        var kno = zy.kNo
        while (kno + length < qz.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }

        points.add(qz)

        //QZ --> YZ
        kno = qz.kNo
        while (kno + length < yz.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }

        points.add(yz)

        return points
    }
}
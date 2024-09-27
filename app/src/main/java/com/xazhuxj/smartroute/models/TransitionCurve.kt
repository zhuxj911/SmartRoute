package com.xazhuxj.smartroute.models

import kotlin.math.*

/**
 * 缓和曲线
 *
 */
class TransitionCurve private constructor(JD: Point, radius: Double, val l0:Double) : Curve(JD, radius) {
    val ZH = Point(note ="ZH") ////参数4：直缓点的x,y
    //以下为计算参数
    val HY = Point(note ="HY" )
    val QZ = Point(note ="QZ" )
    val YH = Point(note ="YH" ) //圆缓点在左切线坐标系中的坐标
    val HZ = Point(note ="HZ" ) ////在ZH点坐标系中的坐标

    override val T //切线长
        get() = m + (radius + P) * tan(alpha * 0.5)

    override val L //曲线长
        get() = radius * (alpha - 2 * beta0) + 2 * l0

    override val E //外矢距
        get() = (radius + P) / cos(0.5 * alpha) - radius

    val beta0
        get() = l0 / radius * 0.5

    val m
        get()=0.5 * l0 - l0.pow(3) / 240 / radius.pow(2)

    val P
        get() = l0.pow(2) / 24.0 / radius

    init {
        JD.note="JD"
    }

    constructor (start: Point, JD: Point, radius:Double, l0:Double, alpha:Double) : this(JD, radius, l0) {
        flag = if (alpha >= 0.0) 1 else -1
        this.alpha = flag * dmsToRadian(alpha)

        //计算偏转角
        val a12 = azimuth(start, JD)
        alpha0 = a12

        var a23 = a12 + this.alpha * flag
        if(a23 < 0) a23 += 2*PI
        if(a23 >= 2 * PI) a23 -= 2*PI

        with(ZH){
            kNo = JD.kNo - T
            x = JD.x - T * cos(a12)
            y = JD.y - T *sin(a12)
        }

        HZ.kNo = ZH.kNo + L
        HY.kNo = ZH.kNo + l0
        QZ.kNo = ZH.kNo + 0.5 * L
        YH.kNo = HZ.kNo - l0

        with(HZ){
            x = JD.x + T * cos(a23)
            y = JD.y + T *sin(a23)
        }

        with(HY){//计算 HY 点坐标
            calHXY(l0, this)
            transformXY(ZH, alpha0, this)
        }

        with(QZ){
            calRXY(kNo - ZH.kNo, this)
            transformXY(ZH, alpha0, this)
        }

        with(YH){
            calHXY(l0, this)
            HZtoZH(this)
            transformXY(ZH, alpha0, this)
        }
    }

    override fun toString(): String {
        return  "$JD" +
                "$ZH" +
                "$HY" +
                "$QZ" +
                "$YH" +
                "$HZ" +
                "R=$radius\n" +
                "l0=$l0\n" +
                "α=${radianToDmsString(alpha)},${if(flag==1) "右偏" else "左偏"}\n" +
                "T=$T\n" +
                "L=$L\n" +
                "E=$E\n" +
                "q=$q\n" +
                "β0=${radianToDmsString(beta0)}\n" +
                "m=$m\n" +
                "P=$P\n" +
                "α0=${radianToDmsString(alpha0)}\n"
    }


    /**
     * 计算缓和曲线上的点坐标
     *
     * @param li 曲线长
     * @param pt 计算点
     */
    private fun calHXY(li: Double, pt: Point) {
        pt.x = li - li.pow(5) / (radius*l0).pow(2) / 40 + li.pow(9) / (radius*l0).pow( 4) / 3456
        pt.y = flag *(
                (li.pow(3) / 6 / (radius * l0)
                - li.pow(7) / (radius * l0).pow(3) / 336
                + li.pow(11) / (radius * l0).pow(5) / 42240)
                )
    }

    /**
     * 计算圆曲线上的点坐标
     *
     * @param li 曲线长
     * @param pt 计算点
     */
    private fun calRXY(li: Double, pt: Point) {
        val betai = (li - l0) / radius + beta0
        pt.x = radius * sin(betai) + m
        pt.y = flag * (radius * (1 - cos(betai)) + P)
    }

    /**
     * 计算曲线上的点坐标
     *
     * @param pt 计算点
     */
    private fun calPointInCurve(pt:Point) {
//        if (pt.kNo < ZH.kNo || pt.kNo > HZ.kNo)
//            throw RangeException(-1, "计算点的里程桩号:${pt.kNo} 不在该缓和曲线的范围内:${HZ.kNo}-${ZH.kNo}")

        val li = pt.kNo - ZH.kNo
        if (pt.kNo in ZH.kNo .. HY.kNo) { //ZH --> HY 缓和曲线段
            calHXY(li, pt)
        } else if (pt.kNo > HY.kNo && pt.kNo <= QZ.kNo) { //HY --> QZ 圆曲线段
            calRXY(li, pt)
        } else if (pt.kNo > QZ.kNo && pt.kNo <= YH.kNo) { //QZ --> YH 右边的圆曲线段{
            calRXY(L - li, pt)
            HZtoZH(pt)
        } else{ //(li > (L - l0) && li <=L) YH --> HZ 右边的缓和曲线段  (li > (L - l0) && li <=L)
            calHXY(L - li, pt)
            HZtoZH(pt)
        }
        transformXY(ZH, alpha0, pt)
    }

    /**
     * 计算曲线上的点坐标
     *
     * @param kno 里程桩号
     * @return 计算点
     */
    override fun calPointOnCurveByKno(kno: Double): Point?{
        if (kno < ZH.kNo || kno > HZ.kNo) return null //不是圆曲线上有效范围

        if (abs(kno - ZH.kNo) < 0.001) {
            return ZH
        } else if (abs(kno - HY.kNo) < 0.001) {
            return HY
        } else if (abs(kno - QZ.kNo) < 0.001) {
            return QZ
        } else if (abs(kno - YH.kNo) < 0.001) {
            return YH
        } else if (abs(kno - HZ.kNo) < 0.001) {
            return HZ
        }

        return Point(kNo = kno).also(::calPointInCurve)
    }


    override fun calAllPoints(length: Double): MutableList<Point>{
        val points: MutableList<Point> = ArrayList<Point>()

        points.add(ZH)

        //ZH --> HY
        var kno = ZH.kNo
        while (kno + length < HY.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(HY)

        //HY --> QZ
        kno = HY.kNo
        while (kno + length < QZ.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }

        points.add(QZ)

        //QZ --> YH
        kno = QZ.kNo
        while (kno + length < YH.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }
        points.add(YH)

        //YH--> HZ
        kno = YH.kNo
        while (kno + length < HZ.kNo) {
            kno += length
            points.add(Point(kNo = kno).also(::calPointInCurve))
        }
        points.add(HZ)

        return points
    }

    /**
     * 将HZ坐标系转换到ZH坐标系
     *
     * @param pt 转换计算点
     */
    fun HZtoZH(pt:Point) {
        val xi = T*(1+cos(alpha)) - pt.x * cos(alpha) - pt.y * sin(alpha)
        val yi = T * sin(alpha) - pt.x * sin(alpha) + pt.y * cos(alpha)
        pt.x = xi; pt.y = yi
//        transformXY(T*(1+cos(alpha)), T * sin(alpha), alpha+PI, pt)
    }
}
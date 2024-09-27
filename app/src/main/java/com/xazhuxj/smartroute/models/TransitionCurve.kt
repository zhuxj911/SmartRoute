package com.xazhuxj.smartroute.models

import kotlin.math.*
/**
 * 缓和曲线
 *
 */
class TransitionCurve private constructor(jd: GPoint, radius: Double, val l0:Double) : Curve(jd, radius) {
    val zh = GPoint(note ="ZH") ////参数4：直缓点的x,y
    //以下为计算参数
    val hy = GPoint(note ="HY" )
    val qz = GPoint(note ="QZ" )
    val yh = GPoint(note ="YH" ) //圆缓点在左切线坐标系中的坐标
    val hz = GPoint(note ="HZ" ) ////在ZH点坐标系中的坐标

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
        jd.note="JD"
    }

    constructor (start: GPoint, jd: GPoint, end: GPoint, radius:Double, l0:Double) : this(jd, radius, l0) {
        //判断 start -> JD -> end 是偏右？ 还是 偏左？
        flag = isRight(start, jd, end)

        //计算偏转角
        val a12 = azimuth(start, jd)
        alpha0 = a12 // //start(ZH)->JD的坐标方位角，单位弧度， 用于将ZD坐标系转换为测量坐标系
        val a23 = azimuth(jd, end)
        if(flag == 1) {
            alpha = a23 - a12
        }
        else {
            alpha = a12 - a23
        }
        if(alpha <0) alpha += 2* PI

        with(zh){
            kNo = jd.kNo - T
            x = jd.x - T * cos(a12)
            y = jd.y - T *sin(a12)
        }

        with(hz){
            kNo = zh.kNo + L
            x = jd.x + T * cos(a23)
            y = jd.y + T *sin(a23)
        }

        with(hy){//计算 HY 点坐标
            kNo = zh.kNo + l0
            calHXY(l0, this)
            transformXY(zh, alpha0, this)
        }

        with(qz){
            kNo = zh.kNo + 0.5 * L
            calRXY(kNo - zh.kNo, this)
            transformXY(zh, alpha0, this)
        }

        with(yh){
            kNo = hz.kNo - l0
            calHXY(l0, this)
            HZtoZH(this)
            transformXY(zh, alpha0, this)
        }
    }

    constructor (start: GPoint, jd: GPoint, radius:Double, l0:Double, alpha:Double) : this(jd, radius, l0) {
        flag = if (alpha >= 0.0) 1 else -1
        this.alpha = flag * dmsToRadian(alpha)

        //计算偏转角
        val a12 = azimuth(start, jd)
        alpha0 = a12

        var a23 = a12 + this.alpha * flag
        if(a23 < 0) a23 += 2*PI
        if(a23 >= 2 * PI) a23 -= 2*PI

        with(zh){
            kNo = jd.kNo - T
            x = jd.x - T * cos(a12)
            y = jd.y - T *sin(a12)
        }

        hz.kNo = zh.kNo + L
        hy.kNo = zh.kNo + l0
        qz.kNo = zh.kNo + 0.5 * L
        yh.kNo = hz.kNo - l0

        with(hz){
            x = jd.x + T * cos(a23)
            y = jd.y + T *sin(a23)
        }

        with(hy){//计算 HY 点坐标
            calHXY(l0, this)
            transformXY(zh, alpha0, this)
        }

        with(qz){
            calRXY(kNo - zh.kNo, this)
            transformXY(zh, alpha0, this)
        }

        with(yh){
            calHXY(l0, this)
            HZtoZH(this)
            transformXY(zh, alpha0, this)
        }
    }

    override fun toString(): String {
        return  "$jd" +
                "$zh" +
                "$hy" +
                "$qz" +
                "$yh" +
                "$hz" +
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
    private fun calHXY(li: Double, pt: GPoint) {
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
    private fun calRXY(li: Double, pt: GPoint) {
        val betai = (li - l0) / radius + beta0
        pt.x = radius * sin(betai) + m
        pt.y = flag * (radius * (1 - cos(betai)) + P)
    }

    /**
     * 计算曲线上的点坐标
     *
     * @param pt 计算点
     */
    private fun calPointInCurve(pt:GPoint) {
//        if (pt.kNo < ZH.kNo || pt.kNo > HZ.kNo)
//            throw RangeException(-1, "计算点的里程桩号:${pt.kNo} 不在该缓和曲线的范围内:${HZ.kNo}-${ZH.kNo}")

        val li = pt.kNo - zh.kNo
        if (pt.kNo in zh.kNo .. hy.kNo) { //ZH --> HY 缓和曲线段
            calHXY(li, pt)
        } else if (pt.kNo > hy.kNo && pt.kNo <= qz.kNo) { //HY --> QZ 圆曲线段
            calRXY(li, pt)
        } else if (pt.kNo > qz.kNo && pt.kNo <= yh.kNo) { //QZ --> YH 右边的圆曲线段{
            calRXY(L - li, pt)
            HZtoZH(pt)
        } else{ //(li > (L - l0) && li <=L) YH --> HZ 右边的缓和曲线段  (li > (L - l0) && li <=L)
            calHXY(L - li, pt)
            HZtoZH(pt)
        }
        transformXY(zh, alpha0, pt)
    }

    /**
     * 计算曲线上的点坐标
     *
     * @param kno 里程桩号
     * @return 计算点
     */
    override fun calPointOnCurveByKno(kno: Double): GPoint?{
        if (kno < zh.kNo || kno > hz.kNo) return null //不是圆曲线上有效范围

        if (abs(kno - zh.kNo) < 0.001) {
            return zh
        } else if (abs(kno - hy.kNo) < 0.001) {
            return hy
        } else if (abs(kno - qz.kNo) < 0.001) {
            return qz
        } else if (abs(kno - yh.kNo) < 0.001) {
            return yh
        } else if (abs(kno - hz.kNo) < 0.001) {
            return hz
        }

        return GPoint(kNo = kno).also(::calPointInCurve)
    }


    override fun calAllPoints(length: Double): ArrayList<GPoint>{
        val points = ArrayList<GPoint>()

        points.add(zh)

        //ZH --> HY
        var kno = zh.kNo
        while (kno + length < hy.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }

        points.add(hy)

        //HY --> QZ
        kno = hy.kNo
        while (kno + length < qz.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }

        points.add(qz)

        //QZ --> YH
        kno = qz.kNo
        while (kno + length < yh.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }
        points.add(yh)

        //YH--> HZ
        kno = yh.kNo
        while (kno + length < hz.kNo) {
            kno += length
            points.add(GPoint(kNo = kno).also(::calPointInCurve))
        }
        points.add(hz)

        return points
    }

    /**
     * 将HZ坐标系转换到ZH坐标系
     *
     * @param pt 转换计算点
     */
    fun HZtoZH(pt:GPoint) {
        val xi = T*(1+cos(alpha)) - pt.x * cos(alpha) - pt.y * sin(alpha)
        val yi = T * sin(alpha) - pt.x * sin(alpha) + pt.y * cos(alpha)
        pt.x = xi; pt.y = yi
//        transformXY(T*(1+cos(alpha)), T * sin(alpha), alpha+PI, pt)
    }
}
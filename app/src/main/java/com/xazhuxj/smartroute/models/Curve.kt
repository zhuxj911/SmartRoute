package com.xazhuxj.smartroute.models

abstract class Curve(val jd: GPoint, val radius: Double) : ICurve {
    /**
     * 偏转角, 单位：弧度
     */
    var alpha = 0.0
        protected set

    /**
     * 起始边方位角，单位：弧度
     */
    var alpha0 = 0.0
        protected set

    /**
     * 曲线偏右 =1 还是 偏左=-1 的标识，程序内部计算出
     */
    var flag: Int = 1
        protected set

    /**
     * 切线长
     */
    abstract val T: Double

    /**
     * 曲线长
     */
    abstract val L: Double

    /**
     * 外矢距
     */
    abstract val E: Double

    /**
     * 切曲差
     */
    val q: Double
        get() = 2*T - L
}
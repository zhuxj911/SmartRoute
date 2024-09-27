package com.xazhuxj.smartroute.models

interface ICurve {
    fun calPointOnCurveByKno(kno: Double): Point?
    fun calAllPoints(length: Double=20.0): MutableList<Point>
}
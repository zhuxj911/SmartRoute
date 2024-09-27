package com.xazhuxj.smartroute.models

interface ICurve {
    fun calPointOnCurveByKno(kno: Double): GPoint?
    fun calAllPoints(length: Double=20.0): ArrayList<GPoint>
}
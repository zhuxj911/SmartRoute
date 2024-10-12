package com.xazhuxj.smartroute.ui.home

import androidx.databinding.ObservableDouble
import androidx.lifecycle.MutableLiveData
import com.xazhuxj.smartroute.models.CircleCurve
import com.xazhuxj.smartroute.models.ICurve
import com.xazhuxj.smartroute.models.Point
import com.xazhuxj.smartroute.models.TransitionCurve
import com.xazhuxj.smartroute.models.calculateAlpha
import com.xazhuxj.smartroute.models.radianToDms
import com.xazhuxj.smartroute.utils.ObservableViewModel

class HomeViewModel : ObservableViewModel() {

    lateinit var route: ICurve
    lateinit var ptList: ArrayList<Point>


    private val _dataInputMethod = MutableLiveData(DataInputMethod.DIR)
    val dataInputMethod = _dataInputMethod

    private var _alpha = ObservableDouble(10.182)
    var alpha: ObservableDouble = _alpha

    private var _radius = ObservableDouble(1000.0)
    var radius: ObservableDouble = _radius

    private var _l0 = ObservableDouble(80.0)
    var l0: ObservableDouble = _l0

    private var _knoJD = ObservableDouble(5330.198)
    var knoJD: ObservableDouble = _knoJD

    private var _xJD = ObservableDouble(3088386.436)
    var xJD: ObservableDouble = _xJD

    private var _yJD = ObservableDouble(66798.566)
    var yJD: ObservableDouble = _yJD

    private var _xStart = ObservableDouble(3088256.238)
    var xStart: ObservableDouble = _xStart

    private var _yStart = ObservableDouble(66798.566)
    var yStart: ObservableDouble = _yStart

    private var _xEnd = ObservableDouble(3088514.534)
    var xEnd: ObservableDouble = _xEnd

    private var _yEnd = ObservableDouble(66821.858)
    var yEnd: ObservableDouble = _yEnd

    private var _knoAnypoint = ObservableDouble(5359.866)
    var kno_anypoint: ObservableDouble = _knoAnypoint

    private var _length = ObservableDouble(20.0)
    var length: ObservableDouble = _length

    fun onAlpha() {
        _dataInputMethod.value = DataInputMethod.ALPHA
    }

    fun onDir() {
        _dataInputMethod.value = DataInputMethod.DIR
    }

    /**
     * 计算线路偏转角α， 以简化线路的生成方式
     */
    fun onCalculateAlpha() {
        val radAlpha = calculateAlpha(
            Point(
                x = xStart.get(),
                y = yStart.get()
            ), //误写成  GPoint(xStart.value!!, yStart.value!!)， 导致y值一直为0
            Point(x = xJD.get(), y = yJD.get()),
            Point(x = xEnd.get(), y = yEnd.get()),
        )

        alpha.set(radianToDms(radAlpha)) //将其转换为界面显示的 浮点数 度分秒 形式
    }

    fun onCalculateSinglePoint() {
        //单点坐标计算
        ptList = ArrayList()
        val jd = Point(knoJD.get(), xJD.get(), yJD.get())
        val start = Point(0.0, xStart.get(), yStart.get())

        if (l0.get() <= 0) { //圆曲线

            route = CircleCurve(start, jd, radius.get(), alpha.get())
            val pt = route.calPointOnCurveByKno(kno_anypoint.get())
            pt?.let { ptList.add(it) }

        } else { //缓和曲线
            route = TransitionCurve(
                start,
                jd,
                radius.get(),
                l0.get(),
                alpha.get()
            )

            val pt = route.calPointOnCurveByKno(kno_anypoint.get())
            pt?.let { ptList.add(pt) }
        }
    }


    fun onCalculateAllPoints() {
        // 在这里做耗时操作
        //批量坐标计算，间隔默认为20
        ptList = ArrayList()
        val jd = Point(knoJD.get(), xJD.get(), yJD.get())
        val start = Point(0.0, xStart.get(), yStart.get())

        if (l0.get() <= 0) { //圆曲线
            route = CircleCurve(start, jd, radius.get(), alpha.get())
            ptList = route.calAllPoints(length.get())
        } else { //缓和曲线
            route = TransitionCurve(start, jd, radius.get(), l0.get(), alpha.get())
            ptList = route.calAllPoints(length.get())
        }
    }

    fun onSetCircleCurveData() {
        alpha.set(40.2018)
        radius.set(120.0)
        l0.set(0.0)
        knoJD.set(3135.12)
        xJD.set(6848.320)
        yJD.set(5634.240)
        xStart.set(6821.350)
        yStart.set(5599.3759)
        xEnd.set(6846.31)
        yEnd.set(5678.27)
        kno_anypoint.set(3100.0)
        length.set(10.0)
    }

    fun onSetTransitionCurveData() {
        alpha.set(10.182)
        radius.set(1000.0)
        l0.set(80.0)
        knoJD.set(5330.198)
        xJD.set(3088386.436)
        yJD.set(66798.566)
        xStart.set(3088256.238)
        yStart.set(66798.566)
        xEnd.set(3088514.534)
        yEnd.set(66821.858)
        kno_anypoint.set(5359.866)
        length.set(20.0)
    }
}

enum class DataInputMethod {
    ALPHA, DIR
}
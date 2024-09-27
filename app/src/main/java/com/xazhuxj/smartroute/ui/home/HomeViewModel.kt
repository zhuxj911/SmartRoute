package com.xazhuxj.smartroute.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xazhuxj.smartroute.models.Point
import com.xazhuxj.smartroute.models.calculateAlpha
import com.xazhuxj.smartroute.models.radianToDms

class HomeViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    private val _dataInputMethod = MutableLiveData(DataInputMethod.DIR)
    val dataInputMethod = _dataInputMethod

    private val _alpha = MutableLiveData(10.182)
    val alpha: LiveData<Double> = _alpha

    private val _radius = MutableLiveData(1000.0)
    val radius: LiveData<Double> = _radius

    private val _l0 = MutableLiveData(80.0)
    val l0: LiveData<Double> = _l0

    private val _knoJD = MutableLiveData(5330.198)
    val knoJD: LiveData<Double> = _knoJD

    private val _xJD = MutableLiveData(3088386.436)
    val xJD: LiveData<Double> = _xJD

    private val _yJD = MutableLiveData(66798.566)
    val yJD: LiveData<Double> = _yJD

    private val _xStart = MutableLiveData(3088256.238)
    val xStart: LiveData<Double> = _xStart

    private val _yStart = MutableLiveData(66798.566)
    val yStart: LiveData<Double> = _yStart

    private val _xEnd = MutableLiveData(3088514.534)
    val xEnd: LiveData<Double> = _xEnd

    private val _yEnd = MutableLiveData(66821.858)
    val yEnd: LiveData<Double> = _yEnd

    private val _knoAnypoint = MutableLiveData(5359.866)
    val kno_anypoint: LiveData<Double> = _knoAnypoint

    private val _length = MutableLiveData(20.0)
    val length: LiveData<Double> = _length

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
        _alpha.value = calculateAlpha(
            Point(x=xStart.value!!, y=yStart.value!!), //误写成  GPoint(xStart.value!!, yStart.value!!)， 导致y值一直为0
            Point(x=xJD.value!!, y=yJD.value!!),
            Point(x=xEnd.value!!, y=yEnd.value!!),
        )

        _alpha.value = radianToDms(_alpha.value!!) //将其转换为界面显示的 浮点数 度分秒 形式
    }

    fun onCalculateSinglePoint() {

    }

    fun onCalculateAllPoints() {

    }
}

enum class DataInputMethod {
    ALPHA, DIR
}
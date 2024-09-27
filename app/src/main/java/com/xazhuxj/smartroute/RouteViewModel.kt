package com.xazhuxj.smartroute

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RouteViewModel : ViewModel() {
    var route = mutableStateOf(
        Route(
            dirJdStartAlpha = true, //交点-起点-方向角 or 交点-起点-终点
            kJD = 5330.198,
            xJD = 3088386.436,
            yJD = 66798.566,
            xStart = 3088256.238,
            yStart = 66798.566,
            xEnd = 3088514.534,
            yEnd = 66821.858,
            alpha = 10.182,
            r0 = 1000.0,
            l0 = 80.0,
            kNo = 5359.866,
            length = 20.0
        )
    )
}
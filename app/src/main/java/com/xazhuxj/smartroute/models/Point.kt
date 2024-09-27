package com.xazhuxj.smartroute.models

import android.os.Parcel
import android.os.Parcelable
import kotlin.math.*

data class Point(var kNo: Double = 0.0,
                 var x: Double = 0.0,
                 var y: Double = 0.0,
                 var note : String? = null) : Parcelable {

    /**
     * 输出公里账号信息，将浮点数形式的里程输出为 K3+91.042 形式
     */
    val kNoInfo: String
        get() {
            val k = (kNo / 1000).toInt()
            val kLength = kNo - k*1000
            return "K${k}+${String.format("%.3f", kLength)}"
        }

    override fun toString(): String {
        return "$kNoInfo, ${String.format("%.3f", x)}, ${String.format("%.3f", y)}, $note\n"
    }

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(kNo)
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeString(note)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Point> {
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }
}


fun azimuth(pA : Point, pB: Point ) = azimuth(pA.x, pA.y, pB.x, pB.y).first

fun transformXY(xo:Double, yo:Double, beta:Double, p:Point){
    val x = xo + p.x * cos(beta) - p.y * sin(beta)
    val y = yo + p.x * sin(beta) + p.y * cos(beta)

    p.x = x; p.y = y
}

fun transformXY(o:Point, beta:Double, p:Point){
    transformXY(o.x, o.y, beta, p)
}

/**
 * 计算 p1->p2->p3 的偏转方向
 *
 * @param p1 起点
 * @param p2 中间点
 * @param p3 末点
 * @return 1 左偏：−1 或 右偏或直线：1
 */
fun isRight(p1: Point, p2: Point, p3: Point) = if( (p2.y-p1.y)*(p3.x-p2.x) <= (p3.y-p2.y)*(p2.x-p1.x) )
    1
else  //(p2.y-p1.y)*(p3.x-p2.x) > (p3.y-p2.y)*(p2.x-p1.x)
    -1


/**
 * 计算三点的偏转角α， 右+/左-
 */
fun calculateAlpha(start: Point, JD: Point, end: Point) : Double {
    //判断 start -> JD -> end 是偏右？ 还是 偏左？
    val flag = isRight(start, JD, end)

    //计算偏转角
    val a12 = azimuth(start, JD)
    val a23 = azimuth(JD, end)

    var alpha = if(flag == 1) {
        a23 - a12
    }
    else {
        a12 - a23
    }
    if(alpha <0) alpha += 2*PI

    return alpha * flag
}
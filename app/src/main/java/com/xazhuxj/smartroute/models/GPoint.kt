package com.xazhuxj.smartroute.models

import android.os.Parcel
import android.os.Parcelable
import kotlin.math.*

data class GPoint(var kNo: Double = 0.0,
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
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(kNo)
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GPoint> {
        override fun createFromParcel(parcel: Parcel): GPoint {
            return GPoint(parcel)
        }

        override fun newArray(size: Int): Array<GPoint?> {
            return arrayOfNulls(size)
        }
    }
}

fun azimuth(pA : GPoint, pB: GPoint ) = azimuth(pA.x, pA.y, pB.x, pB.y).first

fun transformXY(o:GPoint, beta:Double, p:GPoint){
    val xy = transformXY(o.x, o.y, beta, 1.0, p.x, p.y)
    p.x = xy.first
    p.y = xy.second
}

/**
 * 计算 p1->p2->p3 的偏转方向
 *
 * @param p1 起点
 * @param p2 中间点
 * @param p3 末点
 * @return 1 左偏：−1 或 右偏或直线：1
 */
fun isRight(p1: GPoint, p2: GPoint, p3: GPoint) = if( (p2.y-p1.y)*(p3.x-p2.x) <= (p3.y-p2.y)*(p2.x-p1.x) )
    1
else  //(p2.y-p1.y)*(p3.x-p2.x) > (p3.y-p2.y)*(p2.x-p1.x)
    -1
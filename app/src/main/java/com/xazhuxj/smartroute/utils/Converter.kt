package com.xazhuxj.smartroute.utils

import androidx.databinding.InverseMethod
import org.apache.commons.lang3.math.NumberUtils

/**
 * 双向数据绑定
 * 定义一个静态类，提供两个方法。一个充当正向数据转换器，另外一个充当反向数据转换器
 */
object Converter {
    @InverseMethod("stringToDouble")
    @JvmStatic fun doubleToString(value: Double): String {
        // Converts Double to String.
        return value.toString()
    }

    @JvmStatic fun stringToDouble(value: String): Double {
        return if(NumberUtils.isCreatable(value))
            NumberUtils.createDouble(value)
        else
            0.0
//        return if( value.isEmpty() || value.isBlank() )
//            0.0
//        else
//            value.toDouble()
    }


    @InverseMethod("chToString")
    @JvmStatic fun stringToCh(value: String): CharSequence {
        // Converts String to CharSequence.
        return value
    }

    @JvmStatic fun chToString(value: CharSequence): String {
        return value.toString()
    }
}

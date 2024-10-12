package com.example.smartroute

import com.xazhuxj.smartroute.models.Point
import com.xazhuxj.smartroute.models.TransitionCurve
import com.xazhuxj.smartroute.models.radianToDmsString
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TransitionCurveUnitTest {

    val start = Point(x=3088256.238, y=66798.566)
    val jd = Point(kNo=5330.198 , x=3088386.436, y=66798.566)
    val end = Point(x=3088514.534, y=66821.858)
    
    @Test
    fun transitionCurveWithJD_ZH_alpha() {
        val r = TransitionCurve(start, jd, 1000.0, 80.0, 10.1820)

        r.jd.let{
            assertEquals(5330.198, it.kNo, 1e-3)
            assertEquals("K5+330.198", it.kNoInfo)
            assertEquals(3088386.436, it.x, 1e-3)
            assertEquals(66798.566, it.y, 1e-3)
            assertEquals("JD", it.note)
        }

        r.zh.let{
            assertEquals(5200.0, it.kNo, 1e-1)
            assertEquals("K5+200.000", it.kNoInfo)
            assertEquals(3088256.238, it.x, 1e-3)
            assertEquals(66798.566, it.y, 1e-3)
            assertEquals("ZH", it.note)
        }

        r.hy.let{
            assertEquals(5280.0, it.kNo, 1e-1)
            assertEquals("K5+280.000", it.kNoInfo)
            assertEquals(3088336.225, it.x, 1e-3)
            assertEquals(66799.632, it.y, 1e-3)
            assertEquals("HY", it.note)
        }

        r.qz.let{
            assertEquals(5329.933, it.kNo, 1e-3)
            assertEquals("K5+329.933", it.kNoInfo)
            assertEquals(3088386.048, it.x, 1e-3)
            assertEquals(66802.874, it.y, 1e-3)
            assertEquals("QZ", it.note)
        }

        r.yh.let{
            assertEquals(5379.866, it.kNo, 1e-3)
            assertEquals("K5+379.866", it.kNoInfo)
            assertEquals( 3088435.646, it.x, 1e-3)
            assertEquals(66808.598, it.y, 1e-3)
            assertEquals("YH", it.note)
        }

        r.hz.let{
            assertEquals(5459.866, it.kNo, 1e-3)
            assertEquals("K5+459.866", it.kNoInfo)
            assertEquals(3088514.534, it.x, 1e-3)
            assertEquals(66821.858, it.y, 1e-3)
            assertEquals("HZ", it.note)
        }
        assertEquals(1000.0, r.radius, 1e-1)
        assertEquals(80.0, r.l0, 1e-1)
        assertEquals("0°0′0.00000″", radianToDmsString(r.alpha0))
        assertEquals("10°18′20.00000″", radianToDmsString(r.alpha))
        assertEquals(1, r.flag)
        assertEquals(130.19809566293094, r.T, 1e-3)
        assertEquals(259.8658756916379, r.L, 1e-3)
        assertEquals(4.325388385369251, r.E, 1e-3)
        assertEquals(0.5303156342239959, r.q, 1e-3)
        assertEquals("2°17′30.59225″", radianToDmsString(r.beta0))
        assertEquals(39.99786666666667, r.m, 1e-3)
        assertEquals(0.26666666666666666, r.P, 1e-3)
    }
}
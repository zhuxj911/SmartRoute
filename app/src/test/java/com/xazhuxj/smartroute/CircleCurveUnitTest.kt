package com.example.smartroute

import com.xazhuxj.smartroute.models.CircleCurve
import com.xazhuxj.smartroute.models.Point
import com.xazhuxj.smartroute.models.radianToDmsString
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CircleCurveUnitTest {

    val zy = Point(x=6821.350, y=5599.3759)
    val jd = Point(kNo = 3135.12, x=6848.320, y=5634.240)
    val yz = Point(x=6846.31, y=5678.27)

    @Test
    fun circleCurveWithJD_ZY_YZ() {

        val r = CircleCurve(zy, jd, yz, 120.0)

        r.jd.let{
            assertEquals(3135.12, it.kNo, 1e-2)
            assertEquals("K3+135.120", it.kNoInfo)
            assertEquals(6848.32, it.x, 1e-2)
            assertEquals(5634.24, it.y, 1e-2)
            assertEquals("JD", it.note)
        }

        r.zy.let{
            assertEquals(3091.041796833269, it.kNo, 1e-2)
            assertEquals("K3+91.042", it.kNoInfo)
            assertEquals(6821.34998871721, it.x, 1e-2)
            assertEquals(5599.3758854147445, it.y, 1e-2)
            assertEquals("ZY", it.note)
        }

        r.qz.let{
            assertEquals(3133.2840927996162, it.kNo, 1e-2)
            assertEquals("K3+133.284", it.kNoInfo)
            assertEquals(6840.8458057929065, it.x, 1e-2)
            assertEquals(5636.604556924692, it.y, 1e-2)
            assertEquals("QZ", it.note)
        }

        r.yz.let{
            assertEquals(3175.5263887659635, it.kNo, 1e-2)
            assertEquals("K3+175.526", it.kNoInfo)
            assertEquals(6846.309892919199, it.x, 1e-2)
            assertEquals(5678.272345655567, it.y, 1e-2)
            assertEquals("YZ", it.note)
        }

        assertEquals(120.0, r.radius, 1e-1)
        assertEquals("52°16′31.28188″", radianToDmsString(r.alpha0))
        assertEquals("40°20′18.31655″", radianToDmsString(r.alpha))
        assertEquals(1, r.flag)
        assertEquals(44.07820316673106, r.T, 1e-3)
        assertEquals(84.48459193269431, r.L, 1e-3)
        assertEquals(7.839305357967348, r.E, 1e-3)
        assertEquals(3.6718144007678006, r.q, 1e-3)

        r.calPointOnCurveByKno(3091.05)?.let{
            assertEquals(3091.041796833269, it.kNo, 1e-4)
            assertEquals("K3+91.042", it.kNoInfo)
            assertEquals(6821.34998871721, it.x, 1e-2)
            assertEquals(5599.3758854147445, it.y, 1e-2)
            assertEquals("ZY", it.note)
        }

        r.calPointOnCurveByKno(3100.0)?.let{
            assertEquals(3100.0, it.kNo, 1e-4)
            assertEquals("K3+100.000", it.kNoInfo)
            assertEquals(6826.561778079592, it.x, 1e-2)
            assertEquals(5606.65938692256, it.y, 1e-2)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3120.0)?.let{
            assertEquals(3120.0, it.kNo, 1e-4)
            assertEquals("K3+120.000", it.kNoInfo)
            assertEquals(6836.146806434231, it.x, 1e-4)
            assertEquals(5624.186570450684, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3133.28)?.let{
            assertEquals(3133.2840927996162, it.kNo, 1e-4)
            assertEquals("K3+133.284", it.kNoInfo)
            assertEquals(6840.8458057929065, it.x, 1e-4)
            assertEquals(5636.604556924692, it.y, 1e-4)
            assertEquals("QZ", it.note)
        }

        r.calPointOnCurveByKno(3140.0)?.let{
            assertEquals(3140.0, it.kNo, 1e-4)
            assertEquals("K3+140.000", it.kNoInfo)
            assertEquals(6842.691325306174, it.x, 1e-4)
            assertEquals(5643.061002991095, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3160.0)?.let{
            assertEquals(3160.0, it.kNo, 1e-4)
            assertEquals("K3+160.000", it.kNoInfo)
            assertEquals(6846.013962930339, it.x, 1e-4)
            assertEquals(5662.759607261607, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3175.52)?.let{
            assertEquals(3175.52, it.kNo, 1e-2)
            assertEquals("K3+175.526", it.kNoInfo)
            assertEquals(6846.310184097475, it.x, 1e-2)
            assertEquals(5678.265963528513, it.y, 1e-2)
            assertEquals("YZ", it.note)
        }
    }

    @Test
    fun circleCurveWithJD_ZY_alpha() {
        val r = CircleCurve(zy, jd, 120.0, 40.2018)

        r.jd.let{
            assertEquals(3135.12, it.kNo, 1e-2)
            assertEquals("K3+135.120", it.kNoInfo)
            assertEquals(6848.32, it.x, 1e-2)
            assertEquals(5634.24, it.y, 1e-2)
            assertEquals("JD", it.note)
        }

        r.zy.let{
            assertEquals(3091.041796833269, it.kNo, 1e-2)
            assertEquals("K3+91.042", it.kNoInfo)
            assertEquals(6821.34998871721, it.x, 1e-2)
            assertEquals(5599.3758854147445, it.y, 1e-2)
            assertEquals("ZY", it.note)
        }

        r.qz.let{
            assertEquals(3133.2840927996162, it.kNo, 1e-2)
            assertEquals("K3+133.284", it.kNoInfo)
            assertEquals(6840.8458057929065, it.x, 1e-2)
            assertEquals(5636.604556924692, it.y, 1e-2)
            assertEquals("QZ", it.note)
        }

        r.yz.let{
            assertEquals(3175.5263887659635, it.kNo, 1e-2)
            assertEquals("K3+175.526", it.kNoInfo)
            assertEquals(6846.309892919199, it.x, 1e-2)
            assertEquals(5678.272345655567, it.y, 1e-2)
            assertEquals("YZ", it.note)
        }

        assertEquals(120.0, r.radius, 1e-1)
        assertEquals("52°16′31.28188″", radianToDmsString(r.alpha0))
        assertEquals("40°20′18.00000″", radianToDmsString(r.alpha))
        assertEquals(1, r.flag)
        assertEquals(44.07820316673106, r.T, 1e-3)
        assertEquals(84.48459193269431, r.L, 1e-3)
        assertEquals(7.839305357967348, r.E, 1e-3)
        assertEquals(3.6718144007678006, r.q, 1e-3)

        r.calPointOnCurveByKno(3091.05)?.let{
            assertEquals(3091.042, it.kNo, 1e-3)
            assertEquals("K3+91.042", it.kNoInfo)
            assertEquals(6821.34998871721, it.x, 1e-2)
            assertEquals(5599.3758854147445, it.y, 1e-2)
            assertEquals("ZY", it.note)
        }

        r.calPointOnCurveByKno(3100.0)?.let{
            assertEquals(3100.0, it.kNo, 1e-4)
            assertEquals("K3+100.000", it.kNoInfo)
            assertEquals(6826.561778079592, it.x, 1e-2)
            assertEquals(5606.65938692256, it.y, 1e-2)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3120.0)?.let{
            assertEquals(3120.0, it.kNo, 1e-4)
            assertEquals("K3+120.000", it.kNoInfo)
            assertEquals(6836.146806434231, it.x, 1e-4)
            assertEquals(5624.186570450684, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3133.28)?.let{
            assertEquals(3133.2840927996162, it.kNo, 1e-4)
            assertEquals("K3+133.284", it.kNoInfo)
            assertEquals(6840.8458057929065, it.x, 1e-4)
            assertEquals(5636.604556924692, it.y, 1e-4)
            assertEquals("QZ", it.note)
        }

        r.calPointOnCurveByKno(3140.0)?.let{
            assertEquals(3140.0, it.kNo, 1e-4)
            assertEquals("K3+140.000", it.kNoInfo)
            assertEquals(6842.691325306174, it.x, 1e-4)
            assertEquals(5643.061002991095, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3160.0)?.let{
            assertEquals(3160.0, it.kNo, 1e-4)
            assertEquals("K3+160.000", it.kNoInfo)
            assertEquals(6846.013962930339, it.x, 1e-4)
            assertEquals(5662.759607261607, it.y, 1e-4)
            assertEquals(null, it.note)
        }

        r.calPointOnCurveByKno(3175.52)?.let{
            assertEquals(3175.52, it.kNo, 1e-2)
            assertEquals("K3+175.526", it.kNoInfo)
            assertEquals(6846.310184097475, it.x, 1e-2)
            assertEquals(5678.265963528513, it.y, 1e-2)
            assertEquals("YZ", it.note)
        }
    }
}
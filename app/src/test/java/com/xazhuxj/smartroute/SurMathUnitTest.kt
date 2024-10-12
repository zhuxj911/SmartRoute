package com.xazhuxj.smartroute

import com.xazhuxj.smartroute.models.azimuth
import com.xazhuxj.smartroute.models.dmsToDMS
import com.xazhuxj.smartroute.models.dmsToRadian
import com.xazhuxj.smartroute.models.radianToDMS
import com.xazhuxj.smartroute.models.radianToDms
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SurMathUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDms2DMS() {
        var dms = dmsToDMS(101.02201 )
        assertEquals(101, dms.first)
        assertEquals(2, dms.second)
        assertEquals(20.1, dms.third, 1e-10)

        dms = dmsToDMS(-101.02201 )
        assertEquals(-101, dms.first)
        assertEquals(-2, dms.second)
        assertEquals(-20.1, dms.third, 1e-10)

        dms = dmsToDMS(1.4)
        assertEquals(1, dms.first)
        assertEquals(40, dms.second)
        assertEquals(0.0, dms.third, 1e-10)

        dms = dmsToDMS(-1.4)
        assertEquals(-1, dms.first)
        assertEquals(-40, dms.second)
        assertEquals(0.0, dms.third, 1e-10)
    }

    @Test
    fun testDms2Radian() {
        var r = dmsToRadian(101.02201)
        assertEquals(1.76346176848151, r, 1e-14)

        r = dmsToRadian(-101.02201)
        assertEquals(-1.76346176848151, r, 1e-14)

        r = dmsToRadian(1.4)
        assertEquals(0.0290888208665722, r, 1e-15)

        r = dmsToRadian(-1.4)
        assertEquals(-0.0290888208665722, r, 1e-15)
    }


    @Test
    fun testRadian2DMS() {
        var r = dmsToRadian(101.02201)
        var dms = radianToDMS(r)
        assertEquals(101, dms.first)
        assertEquals(2, dms.second)
        assertEquals(20.1, dms.third, 1e-10)

        r = dmsToRadian(-101.02201)
        dms = radianToDMS(r)
        assertEquals(-101, dms.first)
        assertEquals(-2, dms.second)
        assertEquals(-20.1, dms.third, 1e-10)

        r = dmsToRadian(1.4)
        dms = radianToDMS(r)
        assertEquals(1, dms.first)
        assertEquals(40, dms.second)
        assertEquals(0.0, dms.third, 1e-10)

        r = dmsToRadian(-1.4)
        dms = radianToDMS(r)
        assertEquals(-1, dms.first)
        assertEquals(-40, dms.second)
        assertEquals(0.0, dms.third, 1e-10)
    }

    @Test
    fun testRadian2Dms() {
        var r = dmsToRadian(101.02201)
        var dms = radianToDms(r)
        assertEquals(101.02201, dms, 1e-10)

        r = dmsToRadian(-101.02201)
        dms = radianToDms(r)
        assertEquals(-101.02201, dms, 1e-10)

        r = dmsToRadian(1.4)
        dms = radianToDms(r)
        assertEquals(1.4, dms, 1e-10)

        r = dmsToRadian(-1.4)
        dms = radianToDms(r)
        assertEquals(-1.4, dms, 1e-10)
    }

    @Test
    fun testAzimuth() {
        var ad = azimuth(0.0,0.0, 1.0,1.0) //0.78539816339744800
        assertEquals(0.7853981633974483, ad.first, 1e-15)
        assertEquals(1.4142135623730951, ad.second, 1e-15)

        ad = azimuth(0.0,0.0, -1.0,1.0) //2.35619449019234000
        assertEquals(2.356194490192345, ad.first, 1e-15)
        assertEquals(1.4142135623730951, ad.second, 1e-15)

        ad = azimuth(0.0,0.0, -1.0,-1.0) //3.92699081698724000
        assertEquals(3.9269908169872414, ad.first, 1e-15)
        assertEquals(1.4142135623730951, ad.second, 1e-15)

        ad = azimuth(0.0,0.0, 1.0,-1.0) //5.49778714378214000
        assertEquals(5.497787143782138, ad.first, 1e-15)
        assertEquals(1.4142135623730951, ad.second, 1e-15)
    }
}
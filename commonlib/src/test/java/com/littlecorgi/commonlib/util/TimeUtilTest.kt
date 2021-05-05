package com.littlecorgi.commonlib.util

import org.junit.Test
import kotlin.test.assertEquals

/**
 *
 * @author littlecorgi 2021/5/5
 */
class TimeUtilTest {

    @Test
    fun getTimeFromTimestamp() {
        val date = TimeUtil.getTimeFromTimestamp(1619366918874)
        println(date)
        assertEquals(date, "2021-04-26 00:08:38")
    }

    @Test
    fun getTimestampFromTime() {
        val date = TimeUtil.getTimestampFromTime("2021-04-26 00:08:38")
        println(date)
        assertEquals(date, 1619366918000)
    }
}
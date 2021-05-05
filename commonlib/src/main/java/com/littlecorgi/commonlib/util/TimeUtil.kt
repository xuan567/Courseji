package com.littlecorgi.commonlib.util

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 时间相关的工具类
 * @author littlecorgi 2020/10/21
 */
object TimeUtil {

    /**
     * 根据int型的周数返回String
     */
    fun getWeekDayStr(weekDay: Int): String = when (weekDay) {
        1 -> "周一"
        2 -> "周二"
        3 -> "周三"
        4 -> "周四"
        5 -> "周五"
        6 -> "周六"
        7 -> "周日"
        else -> "时间好像错误了，麻烦反馈给我们的客服小姐姐哦OvO"
    }

    /**
     * 获取今日日期
     */
    fun getTodayDate(): String = SimpleDateFormat("M月d日", Locale.CHINA).format(Date())

    /**
     * 获取今天周几 - Int
     */
    fun getTodayWeekDayInt(): Int {
        val cal = Calendar.getInstance()
        val weekDay = cal.get(Calendar.DAY_OF_WEEK)
        return if (weekDay == 1) {
            7
        } else {
            weekDay - 1
        }
    }

    /**
     * 获取今天周几 - String
     */
    fun getTodayWeekDay(): String = getWeekDayStr(getTodayWeekDayInt())

    /**
     * 根据时间戳返回格式化之后的时间
     *
     * @param timestamp 时间戳，必须是13位 (10位 * 1000 = 13 位)
     */
    fun getTimeFromTimestamp(timestamp: Long): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(timestamp)

    /**
     * 根据时间返回时间戳
     *
     * @param time 北京时间，格式为 yyyy-MM-dd HH:mm:ss
     * @return 13为的毫秒级时间戳
     */
    fun getTimestampFromTime(time: String) =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(time, ParsePosition(0))!!.time
}

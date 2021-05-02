package com.littlecorgi.courseji.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

fun Context.getPrefer(name: String = "config"): SharedPreferences =
    getSharedPreferences(name, MODE_PRIVATE)

object Const {

    const val REQUEST_CODE_IMPORT = 101
    const val REQUEST_CODE_SCHEDULE_SETTING = 102

    const val KEY_SCHOOL_URL = "school_url"
    const val KEY_SCHEDULE_DETAIL_TIME = "schedule_detail_time"
}

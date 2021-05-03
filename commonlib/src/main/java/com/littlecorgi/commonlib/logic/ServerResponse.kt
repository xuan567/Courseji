package com.littlecorgi.commonlib.logic

/**
 *
 * @author littlecorgi 2021/5/3
 */
data class ServerResponse(
    val status: Int = 0,
    val data: String = "",
    val msg: String = "",
    val errorMsg: String = ""
)

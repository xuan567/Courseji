package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 加入班级的响应
 *
 * @author littlecorgi 2021/5/24
 */
data class JoinClassResponse(
    var status: Int? = null, // 状态码
    var msg: String? = null, // 信息
    var errorMsg: String? = null, // 错误信息
    var data: Long? = null // 内容
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230604L
    }
}
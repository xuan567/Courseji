package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 注册新用户 - 响应数据类
 *
 * @author littlecorgi 2021/5/4
 */
data class SignUpResponse(
    val status: Int, // 状态
    val msg: String, // 信息（包含错误信息）
    val errorMsg: String? = null // 错误信息
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230606L
    }
}
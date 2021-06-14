package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 登录的响应类
 *
 * @author littlecorgi_twk 2021/05/23
 */
data class StudentResponse(
    var msg: String? = null,
    var status: Int,
    var `data`: Student
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230608L
    }
}

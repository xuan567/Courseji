package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 获取该学生加入的所有班级 - 数据类
 * @author littlecorgi 2021/6/10
 */
data class AllClassResponse(
    val `data`: List<ClassDetail>,
    val msg: String,
    val errorMsg: String,
    val status: Int
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230601L
    }
}

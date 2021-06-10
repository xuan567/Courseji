package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 班级信息 - 数据类
 *
 * @author littlecorgi 2021/6/10
 */
data class ClassDetail(
    val createdTime: Long,
    val id: Int,
    val lastModifiedTime: Long,
    val name: String,
    val studentNum: Int,
    val teacher: Teacher
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230602L
    }
}

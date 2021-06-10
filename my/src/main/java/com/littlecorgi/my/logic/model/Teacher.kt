package com.littlecorgi.my.logic.model

/**
 * 教师信息 - 数据类
 *
 * @author littlecorgi 2021/6/10
 */
data class Teacher(
    val avatar: String,
    val createdTime: Long,
    val email: String,
    val id: Int,
    val lastModifiedTime: Long,
    val name: String,
    val password: String,
    val phone: String
)
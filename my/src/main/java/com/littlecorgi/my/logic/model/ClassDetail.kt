package com.littlecorgi.my.logic.model

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
)

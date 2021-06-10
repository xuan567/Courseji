package com.littlecorgi.my.logic.model

/**
 * 获取该学生加入的所有班级 - 数据类
 * @author littlecorgi 2021/6/10
 */
data class AllClassResponse(
    val `data`: List<ClassDetail>,
    val msg: String,
    val errorMsg: String,
    val status: Int
)

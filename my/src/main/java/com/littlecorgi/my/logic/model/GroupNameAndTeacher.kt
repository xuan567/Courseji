package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 班级名和教师名 - 数据类，用于加入班级页面展示所有班级列表
 *
 * @author littlecorgi 2021/5/4
 */
data class GroupNameAndTeacher(
    var id: Long = 0,
    var name: String,
    var teacherName: String
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230603L
    }
}
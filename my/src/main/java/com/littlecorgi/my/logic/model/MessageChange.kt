package com.littlecorgi.my.logic.model

import java.io.Serializable

/**
 * 修改信息的数据Bean
 *
 * @author haa-zzz 2020/12/28
 */
data class MessageChange(
    var myImagePath: String? = null, // 头像
    var phone: String? = null // 手机号
) : Serializable {
    companion object {
        private const val serialVersionUID = 5990939387657230605L
    }
}
package com.littlecorgi.my.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册的接口返回信息
 *
 * @author littlecorgi 2021/5/4
 */
@NoArgsConstructor
@Data
public class SignUpResponse implements Serializable {
    private static final long serialVersionUID = 1234567890603L;
    private Integer status; // 状态
    private String msg; // 信息（包含错误信息）
    private String data; // 注册数据
    private String errorMsg; // 错误信息
}

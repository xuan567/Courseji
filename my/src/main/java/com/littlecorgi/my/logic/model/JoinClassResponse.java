package com.littlecorgi.my.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加入班级的响应
 *
 * @author littlecorgi 2021/5/4
 */
@NoArgsConstructor
@Data
public class JoinClassResponse implements Serializable {

    private static final long serialVersionUID = 1234567890605L;

    private Integer status;
    private String msg;
    private String errorMsg;
    private long data;
}

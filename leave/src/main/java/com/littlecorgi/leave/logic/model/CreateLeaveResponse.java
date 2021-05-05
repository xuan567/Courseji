package com.littlecorgi.leave.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建请假的响应
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class CreateLeaveResponse implements Serializable {

    private static final long serialVersionUID = 1234567890406L;

    private Integer status;
    private String msg;
    private LeaveBean data;
}

package com.littlecorgi.leave.logic.model;

import java.io.Serializable;
import java.util.List;

/**
 * 获取所有请假的相应
 *
 * @author littlecorgi 2021/5/5
 */
@lombok.NoArgsConstructor
@lombok.Data
public class AllLeaveResponse implements Serializable {
    private static final long serialVersionUID = 1234567890401L;
    private int status;
    private String msg;
    private List<LeaveBean> data;
}

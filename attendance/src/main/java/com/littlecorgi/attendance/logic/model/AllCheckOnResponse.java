package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取所有签到纪录的响应
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class AllCheckOnResponse implements Serializable {
    private static final long serialVersionUID = 1234567890201L;
    private long status;
    private String msg;
    private String errorMsg;
    private List<CheckOnBean> data;
}

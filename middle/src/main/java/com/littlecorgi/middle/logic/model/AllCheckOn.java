package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 该学生所有的签到纪录
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class AllCheckOn implements Serializable {
    private static final long serialVersionUID = 1234567890501L;
    private int status;
    private String msg;
    private List<CheckOnDetail> data;
}

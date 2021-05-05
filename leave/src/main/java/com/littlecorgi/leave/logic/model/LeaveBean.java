package com.littlecorgi.leave.logic.model;

import java.io.Serializable;

/**
 * 请假具体数据类
 *
 * @author littlecorgi 2021/5/5
 */
@lombok.NoArgsConstructor
@lombok.Data
public class LeaveBean implements Serializable {
    private static final long serialVersionUID = 1234567890403L;
    private int id;
    private int states;
    private String title;
    private String description;
    private String opinion;
    private long startTime;
    private long endTime;
    private StudentBean student;
    private ClassDetailBean classDetail;
}

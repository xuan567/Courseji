package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到的model
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class CheckOnBean implements Serializable {
    private static final long serialVersionUID = 1234567890203L;
    private long id;
    private int checkOnStates;
    private double longitude;
    private double latitude;
    private long createdTime;
    private long lastModifiedTime;
    private StudentBean student;
    private AttendanceBean attendance;
}

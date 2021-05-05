package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每个签到的具体信息
 *
 * @author littlecorgi 2021/5/5
 */
@Data
@NoArgsConstructor
public class CheckOnDetail implements Serializable {
    private static final long serialVersionUID = 1234567890502L;
    private long id;
    private int checkOnStates;
    private double longitude;
    private double latitude;
    private StudentBean student;
    private AttendanceBean attendance;
}

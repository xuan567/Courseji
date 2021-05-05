package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考勤的model
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class AttendanceBean implements Serializable {
    private static final long serialVersionUID = 1234567890202L;
    private int id;
    private String title;
    private String description;
    private long startTime;
    private long endTime;
    private double latitude;
    private double longitude;
    private int radius;
    private ClassDetailBean classDetail;
}

package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应的签到的信息
 *
 * @author littlecorgi 2021/5/5
 */
@Data
@NoArgsConstructor
public class AttendanceBean implements Serializable {
    private static final long serialVersionUID = 1234567890504L;
    private long id;
    private String title;
    private String description;
    private long startTime;
    private long endTime;
    private double latitude;
    private double longitude;
    private int radius;
    private ClassModel classDetail;
}

package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录信息
 */
@Data
@NoArgsConstructor
public class Sign implements Serializable {

    private long checkOnId;
    private long studentId;
    private long attendanceId;
    private int state;
    private int label;
    private int takePhoto;
    private long finishTime;
    private long endTime;
    private double lat;
    private double lng;
    private int radius;
}

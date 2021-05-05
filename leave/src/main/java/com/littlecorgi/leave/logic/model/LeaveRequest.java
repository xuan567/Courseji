package com.littlecorgi.leave.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class LeaveRequest implements Serializable {

    private static final long serialVersionUID = 1234567890407L;

    private int states;
    private String title;
    private String description;
    private long startTime;
    private long endTime;
}

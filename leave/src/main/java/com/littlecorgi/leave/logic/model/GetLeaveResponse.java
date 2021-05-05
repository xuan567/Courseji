package com.littlecorgi.leave.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class GetLeaveResponse implements Serializable {

    private static final long serialVersionUID = 1234567890405L;

    private int status;
    private String msg;
    private LeaveBean data;
}

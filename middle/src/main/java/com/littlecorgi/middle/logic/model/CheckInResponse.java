package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参与签到的响应
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class CheckInResponse implements Serializable {

    private static final long serialVersionUID = 1234567890507L;

    private int status;
    private String msg;
    private CheckOnDetail data;
}

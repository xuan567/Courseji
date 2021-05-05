package com.littlecorgi.middle.logic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参与签到请求携带的定位信息
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class LogAndLat {
    private double latitude;
    private double longitude;
}

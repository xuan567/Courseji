package com.littlecorgi.middle.logic.model;

import lombok.Data;

/**
 * 人脸识别结果返回
 *
 * @author littlecorgi 2021/5/2
 */
@Data
public class FaceRecognitionResponse {

    private static final long serialVersionUID = 1234567890508L;

    int status;

    String msg;

    float data;

    String errorMsg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

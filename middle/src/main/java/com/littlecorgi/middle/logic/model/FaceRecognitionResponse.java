package com.littlecorgi.middle.logic.model;

import lombok.Data;

/**
 * 人脸识别结果返回
 *
 * @author littlecorgi 2021/5/2
 */
@Data
public class FaceRecognitionResponse {
    int status;

    String msg;

    Float data;

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

    public Float getData() {
        return data;
    }

    public void setData(Float data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

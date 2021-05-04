package com.littlecorgi.commonlib.uploadfile.logic;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlecorgi 2021/5/3
 */
@NoArgsConstructor
@Data
public class UploadFileResponse implements Serializable {

    private static final long serialVersionUID = 1234567890301L;

    private Integer status;
    private String msg;
    private String data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

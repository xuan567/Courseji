package com.littlecorgi.middle.logic.model;

import com.google.gson.Gson;
import org.junit.Test;

/**
 * @author littlecorgi 2021/5/2
 */
public class FaceRecognitionResponseTest {

    @Test
    public void testGson() {
        String json = "{\"status\":903,\"msg\":\"腾讯云错误信息\",\"errorMsg\":\"图片中没有人脸。\"}";
        FaceRecognitionResponse faceRecognitionResponse = new Gson()
                .fromJson(json, FaceRecognitionResponse.class);
        System.out.println(faceRecognitionResponse);
    }

}
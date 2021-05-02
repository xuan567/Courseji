package com.littlecorgi.middle.logic.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 人脸识别接口
 *
 * @author littlecorgi 2021/5/2
 */
public interface FaceRecognitionInterface {

    /**
     * 腾讯云/人脸对比，传输文件
     *
     * @param studentId      学生用户id
     * @param detectLiveFace 是否活体检测
     * @param usePersonGroup 是否使用腾讯云人员库
     * @param picFile        用来人脸识别的图片文件
     */
    @Multipart
    @POST("/tencentCloud/compareFaceFromFile")
    Call<ResponseBody> compareFaceFromFile(
            @Query("studentId") Long studentId,
            @Query("detectLiveFace") Boolean detectLiveFace,
            @Query("usePersonGroup") Boolean usePersonGroup,
            @Part MultipartBody.Part picFile);

    /**
     * 腾讯云/人脸对比（戴口罩），传输文件
     *
     * @param studentId      学生用户id
     * @param detectLiveFace 是否活体检测
     * @param usePersonGroup 是否使用腾讯云人员库
     * @param picFile        用来人脸识别的图片文件
     */
    @Multipart
    @POST("/tencentCloud/compareMaskFaceFromFile")
    Call<ResponseBody> compareMaskFaceFromFile(
            @Query("access_token") Long studentId,
            @Query("access_token") Boolean detectLiveFace,
            @Query("access_token") Boolean usePersonGroup,
            @Part MultipartBody.Part picFile);
}

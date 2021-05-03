package com.littlecorgi.middle.logic;

import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.middle.logic.network.FaceRecognitionInterface;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author littlecorgi 2021/5/2
 */
public class RetrofitRepository {

    /**
     * 创建人脸识别的请求
     *
     * @param studentId      学生id
     * @param detectLiveFace 是否使用活体检测
     * @param usePersonGroup 是否使用腾讯云人脸识别人员库
     * @param file           图片文件
     */
    public static Call<ResponseBody> getFaceRecognitionCall(long studentId, boolean detectLiveFace,
                                                            boolean usePersonGroup, File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picFile", file.getName(), requestFile);

        FaceRecognitionInterface faceRecognitionInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(FaceRecognitionInterface.class);
        // 执行请求
        return faceRecognitionInterface
                .compareFaceFromFile(studentId, detectLiveFace, usePersonGroup, body);
    }
}

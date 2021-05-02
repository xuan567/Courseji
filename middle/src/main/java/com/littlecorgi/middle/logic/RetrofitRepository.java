package com.littlecorgi.middle.logic;

import com.littlecorgi.middle.logic.network.FaceRecognitionInterface;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author littlecorgi 2021/5/2
 */
public class RetrofitRepository {

    private static Retrofit retrofit = null;

    private static Retrofit getTencentCloudCESRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://81.71.76.213:10800")
                    .build();
        }
        return retrofit;
    }

    public static Call<ResponseBody> getFaceRecognitionCall(long studentId, boolean detectLiveFace,
                                                            boolean usePersonGroup, File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picFile", file.getName(), requestFile);

        FaceRecognitionInterface faceRecognitionInterface =
                RetrofitRepository.getTencentCloudCESRetrofit()
                        .create(FaceRecognitionInterface.class);
        // 执行请求
        return faceRecognitionInterface
                .compareFaceFromFile(studentId, detectLiveFace, usePersonGroup, body);
    }
}

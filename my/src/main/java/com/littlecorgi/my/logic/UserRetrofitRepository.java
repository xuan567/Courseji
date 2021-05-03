package com.littlecorgi.my.logic;

import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.my.logic.model.Student;
import com.littlecorgi.my.logic.network.UserRequestInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 用户 Retrofit封装类
 *
 * @author littlecorgi 2021/5/2
 */
public class UserRetrofitRepository {

    public static Call<ResponseBody> getUserSignUpCall(Student student) {
        UserRequestInterface userRequestInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(UserRequestInterface.class);
        // 执行请求
        return userRequestInterface.signUp(student);
    }

    public static Call<ResponseBody> getUserSignInCall(String email, String password) {
        UserRequestInterface userRequestInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(UserRequestInterface.class);
        RequestBody body = RequestBody
                .create(MediaType.parse("application/json"), password);
        // 执行请求
        return userRequestInterface.signIn(email, body);
    }
}

package com.littlecorgi.my.logic;

import android.content.SharedPreferences;
import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.logic.model.SignUpResponse;
import com.littlecorgi.my.logic.model.Student;
import com.littlecorgi.my.logic.model.StudentResponse;
import com.littlecorgi.my.logic.network.UserRequestInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 用户 Retrofit封装类
 *
 * @author littlecorgi 2021/5/2
 */
public class UserRetrofitRepository {

    /**
     * 获取用户注册的Call
     *
     * @param student 注册所需的用户信息
     */
    public static Call<SignUpResponse> getUserSignUpCall(Student student) {
        UserRequestInterface userRequestInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(UserRequestInterface.class);
        // 执行请求
        return userRequestInterface.signUp(student);
    }

    /**
     * 获取用户登录Call
     *
     * @param email    账号
     * @param password 密码
     */
    public static Call<StudentResponse> getUserSignInCall(String email, String password) {
        UserRequestInterface userRequestInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(UserRequestInterface.class);
        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody = RequestBody.Companion.create(password, mediaType);
        // 执行请求
        return userRequestInterface.signIn(email, stringBody);
    }

    /**
     * 从SharedPreferences中获取Student数据
     *
     * @param sp SharedPreferences
     */
    public static StudentResponse getStudentFromSP(SharedPreferences sp) {
        Student data = new Student(
                sp.getLong(UserSPConstant.STUDENT_USER_ID, -1),
                sp.getString(UserSPConstant.STUDENT_AVATAR, ""),
                sp.getString(UserSPConstant.STUDENT_EMAIL, ""),
                sp.getString(UserSPConstant.STUDENT_NAME, ""),
                sp.getString(UserSPConstant.STUDENT_PASSWORD, ""),
                sp.getString(UserSPConstant.STUDENT_PHONE, ""),
                sp.getString(UserSPConstant.STUDENT_PICTURE, "")
        );
        return new StudentResponse(null, 800, data);
    }
}

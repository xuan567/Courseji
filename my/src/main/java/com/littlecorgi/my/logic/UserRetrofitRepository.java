package com.littlecorgi.my.logic;

import android.content.SharedPreferences;
import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.commonlib.util.UserSPConstant;
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

    /**
     * 获取用户注册的Call
     *
     * @param student 注册所需的用户信息
     */
    public static Call<ResponseBody> getUserSignUpCall(Student student) {
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
    public static Call<Student> getUserSignInCall(String email, String password) {
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
    public static Student getStudentFromSP(SharedPreferences sp) {
        Student student = new Student();
        student.setStatus(800);
        Student.DataBean data = new Student.DataBean();
        data.setId(sp.getLong(UserSPConstant.STUDENT_USER_ID, -1));
        data.setName(sp.getString(UserSPConstant.STUDENT_NAME, ""));
        data.setPassword(sp.getString(UserSPConstant.STUDENT_PASSWORD, ""));
        data.setPhone(sp.getString(UserSPConstant.STUDENT_PHONE, ""));
        data.setAvatar(sp.getString(UserSPConstant.STUDENT_AVATAR, ""));
        data.setPicture(sp.getString(UserSPConstant.STUDENT_PICTURE, ""));
        student.setData(data);
        return student;
    }
}

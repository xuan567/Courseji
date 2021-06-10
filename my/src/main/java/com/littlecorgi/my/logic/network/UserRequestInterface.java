package com.littlecorgi.my.logic.network;

import com.littlecorgi.my.logic.model.SignUpResponse;
import com.littlecorgi.my.logic.model.Student;
import com.littlecorgi.my.logic.model.StudentResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/2
 */
public interface UserRequestInterface {

    /**
     * 注册
     *
     * @param student 学生信息
     * @return 响应结果，包含Sting类型的结果
     */
    @POST("/student/signUp")
    Call<SignUpResponse> signUp(@Body Student student);

    /**
     * 登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return 响应结果，包含{@link com.littlecorgi.my.logic.model.Student}类型的结果
     */
    @POST("/student/signIn")
    Call<StudentResponse> signIn(@Query("email") String email, @Body RequestBody password);
}

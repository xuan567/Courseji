package com.littlecorgi.my.logic;

import android.util.Log;
import com.littlecorgi.my.logic.model.Student;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    /**
     * 登录
     *
     * @param username 用户名/邮箱
     * @param password 密码
     * @return 是否登录成功
     */
    public Result login(String username, String password) {
        Result result;

        Call<Student> responseBodyCall =
                UserRetrofitRepository.getUserSignInCall(username, password);

        try {
            Response<Student> response = responseBodyCall.execute();
            Log.d("LoginDataSource", "onResponse: " + response);
            if (response.body() != null) {
                Student student = response.body();
                Log.d("LoginDataSource", "onResponse: " + student);
                if (student.getStatus() == 800) {
                    Log.d("LoginDataSource", "onResponse: 登录成功");
                    result = new Result.Success<>(student);
                } else if (student.getStatus() == 1002) {
                    // 用户不存在，转注册
                    student.setData(new Student.DataBean());
                    student.getData().setEmail(username);
                    student.getData().setPassword(password);
                    result = new Result.Success<>(student);
                } else {
                    result = new Result.Error(
                            new IOException("登录失败" + student.getMsg()));
                }
            } else {
                Log.d("LoginDataSource", "onResponse: 响应为空");
                result =
                        new Result.Error(new IOException("登录失败：响应为空"));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            Log.d("LoginDataSource", "onFailure: 登录失败" + exception.getMessage());
            result =
                    new Result.Error(new IOException("登录失败", exception));
        }
        return result;
    }
}
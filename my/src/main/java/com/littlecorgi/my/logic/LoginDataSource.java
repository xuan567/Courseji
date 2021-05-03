package com.littlecorgi.my.logic;

import android.util.Log;
import com.google.gson.Gson;
import com.littlecorgi.my.logic.model.Student;
import java.io.IOException;
import okhttp3.ResponseBody;
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

        Call<ResponseBody> responseBodyCall =
                UserRetrofitRepository.getUserSignInCall(username, password);

        try {
            Response<ResponseBody> response = responseBodyCall.execute();
            Log.d("LoginDataSource", "onResponse: " + response);
            if (response.body() != null) {
                try {
                    String json = response.body().string();
                    Log.d("LoginDataSource", "onResponse: json= " + json);
                    Student student =
                            new Gson().fromJson(json, Student.class);
                    Log.d("LoginDataSource", "onResponse: " + student);
                    if (student.getStatus() == 800) {
                        Log.d("LoginDataSource", "onResponse: 登录成功");
                        result = new Result.Success<>(student);
                    } else {
                        result = new Result.Error(
                                new IOException("登录失败" + student.getMsg()));
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    Log.d("LoginDataSource", "onResponse: " + exception.getMessage());
                    exception.printStackTrace();
                    result =
                            new Result.Error(new IOException("登录失败", exception));
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
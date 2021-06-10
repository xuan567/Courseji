package com.littlecorgi.my.logic;

import android.content.Context;
import android.content.SharedPreferences;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.logic.model.LoggedInUser;
import com.littlecorgi.my.logic.model.Student;
import com.littlecorgi.my.logic.model.StudentResponse;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取单例
     *
     * @param dataSource 数据源
     * @return 单例
     */
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    private void setLoggedInUser(Context context, Student user) {
        SharedPreferences sp = context.getSharedPreferences(
                UserSPConstant.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (user.getId() != null) {
            editor.putLong(UserSPConstant.STUDENT_USER_ID, user.getId());
        }
        if (user.getEmail() != null) {
            editor.putString(UserSPConstant.STUDENT_EMAIL, user.getEmail());
        }
        if (user.getName() != null) {
            editor.putString(UserSPConstant.STUDENT_NAME, user.getName());
        }
        if (user.getPhone() != null) {
            editor.putString(UserSPConstant.STUDENT_PHONE, user.getPhone());
        }
        if (user.getAvatar() != null) {
            editor.putString(UserSPConstant.STUDENT_AVATAR, user.getAvatar());
        }
        if (user.getPicture() != null) {
            editor.putString(UserSPConstant.STUDENT_PICTURE, user.getPicture());
        }
        if (user.getPassword() != null) {
            editor.putString(UserSPConstant.STUDENT_PASSWORD, user.getPassword());
        }
        editor.apply();
    }

    /**
     * 登录
     *
     * @param context  上下文
     * @param username 用户名
     * @param password 密码
     * @return 是否正确
     */
    public Result<LoggedInUser> login(Context context, String username, String password) {
        // handle login
        Result result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            StudentResponse student = ((Result.Success<StudentResponse>) result).getData();
            LoggedInUser loggedInUser = null;
            if (student.getStatus() == 800) {
                setLoggedInUser(context, student.getData());
                loggedInUser = new LoggedInUser("" + student.getData().getId(),
                        student.getData().getName());
            } else if (student.getStatus() == 1002) {
                loggedInUser = new LoggedInUser("" + student.getData().getId(),
                        "用户不存在！");
            }
            this.user = loggedInUser;
            return new Result.Success<LoggedInUser>(loggedInUser);
        }

        return (Result.Error) result;
    }
}
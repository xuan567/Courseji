package com.littlecorgi.my.ui.signup.ui.login;

import android.content.Context;
import android.os.Handler;
import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.littlecorgi.my.R;
import com.littlecorgi.my.ui.signup.data.LoginRepository;
import com.littlecorgi.my.ui.signup.data.Result;
import com.littlecorgi.my.ui.signup.data.model.LoggedInUser;

/**
 * 登录ViewModel
 *
 * @author littlecorgi 2021/05/02
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public void login(Context context, Handler handler, String username, String password) {
        // 可以在一个单独的异步作业中启动
        Result<LoggedInUser> result = loginRepository.login(context, username, password);

        handler.post(() -> {
            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });
    }

    /**
     * 登录数据改变
     *
     * @param username 用户
     * @param password 密码
     */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // 检查用户名是否有效
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // 检查密码是否有效
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 8;
    }
}
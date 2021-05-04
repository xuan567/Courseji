package com.littlecorgi.my.ui.signin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.R;
import com.littlecorgi.my.databinding.ActivityLoginBinding;
import com.littlecorgi.my.ui.signup.SignUpActivity;

/**
 * 登录Activity
 *
 * @author littlecorgi 2021/05/02
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        SharedPreferences sp = getSharedPreferences(UserSPConstant.FILE_NAME, MODE_PRIVATE);
        String email = sp.getString(UserSPConstant.STUDENT_EMAIL, "");
        String password = sp.getString(UserSPConstant.STUDENT_PASSWORD, "");
        if (!email.isEmpty()) {
            usernameEditText.setText(email);
        }
        if (!password.isEmpty()) {
            passwordEditText.setText(password);
        }

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                if (loginResult.getSuccess().getDisplayName().equals("用户不存在！")) {
                    showLoginFailed("用户不存在！转为注册");
                    SignUpActivity
                            .startSignUpActivity(this,
                                    sp.getString(UserSPConstant.STUDENT_EMAIL, ""),
                                    sp.getString(UserSPConstant.STUDENT_PASSWORD, ""));
                } else {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
            setResult(Activity.RESULT_OK);

            // 一旦成功，完成并销毁登录活动
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                new Thread(() -> loginViewModel
                        .login(LoginActivity.this, new Handler(getMainLooper()),
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString())).start();
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            new Thread(() -> loginViewModel
                    .login(LoginActivity.this, new Handler(getMainLooper()),
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString())).start();

        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转到LoginActivity
     *
     * @param context 上下文
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
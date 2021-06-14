package com.littlecorgi.my.ui.addgroup;

import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.my.R;
import com.littlecorgi.my.databinding.MyGroupBinding;
import com.littlecorgi.my.logic.ClassRetrofitRepository;
import com.littlecorgi.my.logic.model.JoinClassResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class AddGroupActivity extends BaseActivity {

    private MyGroupBinding mBinding;
    private long studentId;
    private String code; // 邀请码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.my_group);
        studentId = getIntent().getLongExtra("student_id", -1);
        if (studentId == -1) {
            showErrorToast(this, "用户获取错误", true, Toast.LENGTH_SHORT);
            finish();
        }
        initView();
    }

    private void initView() {
        initBarColor();
        mBinding.myGroupToolbar.setNavigationOnClickListener(v -> finish());
        mBinding.myDescribeSureButton.setOnClickListener(v -> addClass());
        mBinding.myDescribeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void addClass() {
        if (!code.isEmpty()) {
            Dialog loading = DialogUtil.writeLoadingDialog(this, false, "正在加入");
            loading.show();
            loading.setCancelable(false);
            Call<JoinClassResponse> classResponseCall =
                    ClassRetrofitRepository.joinClass(studentId, Long.parseLong(code));
            classResponseCall.enqueue(new Callback<JoinClassResponse>() {
                @Override
                public void onResponse(@NonNull Call<JoinClassResponse> call,
                                       @NonNull Response<JoinClassResponse> response) {
                    loading.cancel();
                    Log.d("AddGroupActivity", "onResponse: " + response.body());
                    JoinClassResponse joinClassResponse = response.body();
                    assert joinClassResponse != null;
                    if (joinClassResponse.getStatus() != null
                            && joinClassResponse.getStatus() == 800) {
                        showSuccessToast(AddGroupActivity.this, "加入成功", true, Toast.LENGTH_SHORT);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            finish();
                        }, 1000);
                    } else {
                        showErrorToast(AddGroupActivity.this,
                                "错误，错误码" + joinClassResponse.getMsg(), true, Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JoinClassResponse> call, @NonNull Throwable t) {
                    loading.cancel();
                    t.printStackTrace();
                    showErrorToast(AddGroupActivity.this, "网络错误", true, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    /**
     * 跳转到AddGroupActivity
     *
     * @param context   上下文
     * @param studentId 学生id
     */
    public static void startAddGroupActivity(Context context, long studentId) {
        Intent intent = new Intent(context, AddGroupActivity.class);
        intent.putExtra("student_id", studentId);
        context.startActivity(intent);
    }
}

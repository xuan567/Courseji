package com.littlecorgi.my.ui.addgroup;

import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class AddGroupActivity extends BaseActivity {

    private MyGroupBinding mBinding;
    private long studentId;
    private long code; // 邀请码
    private boolean[] isCodeOk = {false};

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
        mBinding.myAboutReturnButton.setOnClickListener(v -> finish());
        mBinding.myDescribeSureButton.setOnClickListener(v -> addClass());
        mBinding.myDescribeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                if (!pattern.matcher(s).matches()) {
                    mBinding.myDescribeEditText.setError("手机号不能有数字之外的元素");
                    isCodeOk[0] = false;
                    return;
                }
                isCodeOk[0] = true;
                code = Long.parseLong(s.toString());
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
        if (isCodeOk[0]) {
            Dialog loading = DialogUtil.writeLoadingDialog(this, false, "正在加入");
            loading.show();
            loading.setCancelable(false);
            Call<JoinClassResponse> classResponseCall =
                    ClassRetrofitRepository.joinClass(studentId, code);
            classResponseCall.enqueue(new Callback<JoinClassResponse>() {
                @Override
                public void onResponse(@NonNull Call<JoinClassResponse> call,
                                       @NonNull Response<JoinClassResponse> response) {
                    loading.cancel();
                    Log.d("AddGroupActivity", "onResponse: " + response.body());
                    JoinClassResponse joinClassResponse = response.body();
                    assert joinClassResponse != null;
                    if (joinClassResponse.getStatus() == 800) {
                        showSuccessToast(AddGroupActivity.this, "加入成功", true, Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        showSuccessToast(AddGroupActivity.this,
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

    public static void startAddGroupActivity(Context context, long studentId) {
        Intent intent = new Intent(context, AddGroupActivity.class);
        intent.putExtra("student_id", studentId);
        context.startActivity(intent);
    }
}

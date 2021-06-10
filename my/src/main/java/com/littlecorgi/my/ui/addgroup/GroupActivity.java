package com.littlecorgi.my.ui.addgroup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.R;
import com.littlecorgi.my.databinding.ActivityGroupBinding;
import com.littlecorgi.my.logic.ClassRetrofitRepository;
import com.littlecorgi.my.logic.model.AllClassResponse;
import com.littlecorgi.my.logic.model.ClassDetail;
import com.littlecorgi.my.logic.model.GroupNameAndTeacher;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 班级Activity
 *
 * @author littlecorgi 2021/05/04
 */
public class GroupActivity extends BaseActivity {

    private static final String TAG = "GroupActivity";
    private ActivityGroupBinding mBinding;
    private final ArrayList<GroupNameAndTeacher> mGroupNameAndTeachersList = new ArrayList<>();
    private SharedPreferences sp;
    private GroupAdapter mAdapter;
    private long mStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        sp = getSharedPreferences(UserSPConstant.FILE_NAME, MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.group_add_class) {
                if (mStudentId == -1) {
                    showInfoToast(this, "请登录", true, Toast.LENGTH_SHORT);
                } else {
                    AddGroupActivity.startAddGroupActivity(this, mStudentId);
                }
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        mAdapter = new GroupAdapter(mGroupNameAndTeachersList);
        mBinding.groupRvAllClass.setLayoutManager(new LinearLayoutManager(this));
        mBinding.groupRvAllClass.setAdapter(mAdapter);
    }

    private void initData() {
        Dialog loading = DialogUtil.writeLoadingDialog(this, false, "获取数据");
        loading.show();
        loading.setCancelable(false);
        mStudentId = sp.getLong(UserSPConstant.STUDENT_USER_ID, -1L);
        if (mStudentId == -1) {
            showErrorToast(this, "获取不到用户信息", true, Toast.LENGTH_SHORT);
            loading.cancel();
            finish();
        }
        ClassRetrofitRepository.getAllClassFromTheStudentCall(mStudentId).enqueue(
                new Callback<AllClassResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AllClassResponse> call,
                                           @NonNull Response<AllClassResponse> response) {
                        loading.cancel();
                        Log.d("GroupActivity", "onResponse: " + response.body());
                        AllClassResponse response1 = response.body();
                        assert response1 != null;
                        if (response1.getStatus() == 800) {
                            mGroupNameAndTeachersList.clear();
                            for (ClassDetail dataBean : response1.getData()) {
                                GroupNameAndTeacher groupNameAndTeacher = new GroupNameAndTeacher(
                                        dataBean.getId(),
                                        dataBean.getName(),
                                        dataBean.getTeacher().getName()
                                );
                                mGroupNameAndTeachersList.add(groupNameAndTeacher);
                            }
                            Log.d(TAG, "onResponse: " + mGroupNameAndTeachersList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showErrorToast(GroupActivity.this, "获取数据错误，错误码" + response1.getStatus(),
                                    true, Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AllClassResponse> call,
                                          @NonNull Throwable t) {
                        loading.cancel();
                        t.printStackTrace();
                        Log.e("GroupActivity", "onFailure");
                        showErrorToast(GroupActivity.this, "网络错误", true, Toast.LENGTH_SHORT);
                    }
                });
    }

    public static void startGroupActivity(Context context) {
        Intent intent = new Intent(context, GroupActivity.class);
        context.startActivity(intent);
    }
}

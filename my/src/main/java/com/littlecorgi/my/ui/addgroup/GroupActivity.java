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
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.R;
import com.littlecorgi.my.databinding.ActivityGroupBinding;
import com.littlecorgi.my.logic.ClassRetrofitRepository;
import com.littlecorgi.my.logic.model.AllClassResponse;
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

    private ActivityGroupBinding mBinding;
    private ArrayList<GroupNameAndTeacher> groupNameAndTeachersList;
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
        initMenu();
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(mBinding.toolbar);
    }

    private void initRecyclerView() {
        groupNameAndTeachersList = new ArrayList<>();
        mAdapter = new GroupAdapter(groupNameAndTeachersList);
        mBinding.groupRvAllClass.setAdapter(mAdapter);
    }

    private void initMenu() {
        Menu menu = mBinding.toolbar.getMenu();
        menu.findItem(R.id.group_add_class).setOnMenuItemClickListener(item -> {
            AddGroupActivity.startAddGroupActivity(GroupActivity.this, mStudentId);
            return false;
        });
    }

    private void initData() {
        Dialog loading = DialogUtil.writeLoadingDialog(this, false, "获取数据");
        loading.show();
        loading.setCancelable(false);
        mStudentId = sp.getLong(UserSPConstant.STUDENT_USER_ID, -1);
        if (mStudentId == -1) {
            showErrorToast(this, "获取不到用户信息", true, Toast.LENGTH_SHORT);
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
                            groupNameAndTeachersList.clear();
                            for (AllClassResponse.DataBean dataBean : response1.getData()) {
                                GroupNameAndTeacher groupNameAndTeacher = new GroupNameAndTeacher();
                                groupNameAndTeacher.setName(dataBean.getName());
                                groupNameAndTeacher.setTeacherName(dataBean.getTeacher().getName());
                                groupNameAndTeacher.setId(dataBean.getId());
                                groupNameAndTeachersList.add(groupNameAndTeacher);
                            }
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

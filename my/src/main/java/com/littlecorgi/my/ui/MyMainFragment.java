package com.littlecorgi.my.ui;

import static android.content.Context.MODE_PRIVATE;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;
import static com.littlecorgi.my.ui.about.AboutActivity.startAboutActivity;
import static com.littlecorgi.my.ui.message.MessageActivity.startMessageActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.my.R;
import com.littlecorgi.my.logic.UserRetrofitRepository;
import com.littlecorgi.my.logic.model.Student;
import com.littlecorgi.my.ui.signup.data.LoginDataSource;
import com.littlecorgi.my.ui.signup.data.LoginRepository;
import com.littlecorgi.my.ui.signup.data.Result;
import com.littlecorgi.my.ui.signup.ui.login.LoginActivity;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

/**
 * My 主页
 */
public class MyMainFragment extends Fragment {

    /*
      未完成的：在这里要完成学生个人信息的获取吧信息填充到myMessage中
    */
    private View mView;
    private Student student;
    private long studentId;
    private RefreshLayout refreshLayout;
    private AppCompatTextView mTvName;
    private AppCompatTextView mTvProfessional;
    private AppCompatImageView mIvAvatar;
    private SharedPreferences sp;

    ActivityResultLauncher<Intent> mGetContent =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            sp = requireContext()
                                    .getSharedPreferences(UserSPConstant.FILE_NAME, MODE_PRIVATE);
                            initView();
                            initData();
                            initClick();
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.my_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = requireContext().getSharedPreferences(UserSPConstant.FILE_NAME, MODE_PRIVATE);
        initView();
        initData();
        initClick();
    }

    private void initView() {
        studentId = sp.getLong(UserSPConstant.STUDENT_USER_ID, -1L);
        refreshLayout = mView.findViewById(R.id.refreshLayout);
        if (studentId == -1) {
            // 没有登录
            mView.findViewById(R.id.no_login).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.has_login).setVisibility(View.GONE);
            refreshLayout.setEnableRefresh(false);
        } else {
            mView.findViewById(R.id.no_login).setVisibility(View.GONE);
            mView.findViewById(R.id.has_login).setVisibility(View.VISIBLE);
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
            refreshLayout.setOnRefreshListener(layout -> {
                new Thread(() -> {
                    Result result = LoginRepository.getInstance(new LoginDataSource())
                            .login(requireContext(), student.getData().getEmail(),
                                    student.getData().getPassword());
                    boolean refreshData = false;
                    if (result instanceof Result.Success) {
                        refreshData = true;
                    }
                    boolean finalRefreshData = refreshData;
                    // 切换回主线程更新UI （用Java太恶心人了）
                    new Handler(Looper.getMainLooper()).post(() -> {
                        initData();
                        layout.finishRefresh(finalRefreshData);
                    });
                }).start();
            });
            mTvName = mView.findViewById(R.id.my_name);
            mTvProfessional = mView.findViewById(R.id.my_professional);
            mIvAvatar = mView.findViewById(R.id.my_picture);
        }
        initBarColor();

    }

    private void initBarColor() {
        setWindowStatusBarColor(getActivity(), R.color.blue);
    }

    private void initClick() {
        ConstraintLayout messageLayout = mView.findViewById(R.id.my_message);
        ConstraintLayout aboutLayout = mView.findViewById(R.id.my_about);
        messageLayout.setOnClickListener(v -> {
            if (studentId == -1) {
                mGetContent.launch(new Intent(requireContext(), LoginActivity.class));
            } else {
                startMessageActivity(getContext(), student);
            }
        });
        aboutLayout.setOnClickListener(v -> startAboutActivity(getContext()));
    }

    private void initData() {
        if (studentId != -1) {
            student = UserRetrofitRepository.getStudentFromSP(sp);
            if (student.getData().getAvatar().isEmpty()) {
                Glide.with(this).load(R.drawable.my).into(mIvAvatar);
            } else {
                Glide.with(this).load(student.getData().getAvatar()).into(mIvAvatar);
            }
            if (student.getData().getName().isEmpty()) {
                mTvName.setText("数据异常，请上报");
            } else {
                mTvName.setText(student.getData().getName());
            }
            mTvProfessional.setText("计算机学院");
        }
    }
}

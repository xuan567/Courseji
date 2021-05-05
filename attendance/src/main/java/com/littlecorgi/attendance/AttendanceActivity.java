package com.littlecorgi.attendance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.littlecorgi.attendance.logic.AttendanceRepository;
import com.littlecorgi.attendance.logic.model.AllCheckOnResponse;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.attendance.tools.PieChartManager;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 考勤统计Activity
 */
public class AttendanceActivity extends AppCompatActivity {

    private long mStudentId = 1;
    private static final String TAG = "AttendanceActivity";
    private List<CheckOnBean> mCheckOnBeanList = new ArrayList<>();
    private List<CheckOnBean> mWaitCheckList = new ArrayList<>();
    private List<CheckOnBean> mFinishList = new ArrayList<>();
    private List<CheckOnBean> mLeaveList = new ArrayList<>();
    private List<CheckOnBean> mNoCheckList = new ArrayList<>();

    private TextView mTvProportion;
    private TextView mTvWait;
    private TextView mTvFinish;
    private TextView mTvLeave;
    private TextView mTvLate;
    private TextView mTvName;
    private CircleImageView mIvAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mTvProportion = findViewById(R.id.tv_proportion);
        mTvWait = findViewById(R.id.tv_sum_wait);
        mTvFinish = findViewById(R.id.tv_sum_finish);
        mTvLeave = findViewById(R.id.tv_sum_leave);
        mTvLate = findViewById(R.id.tv_sum_late);
        mTvName = findViewById(R.id.tv_name);
        mIvAvatar = findViewById(R.id.civ_user_icon);
    }

    private void initData() {
        Dialog dialog = DialogUtil.writeLoadingDialog(this, false, "请求数据中");
        dialog.show();
        dialog.setCancelable(false);
        AttendanceRepository.getAllCheckOn(mStudentId).enqueue(new Callback<AllCheckOnResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllCheckOnResponse> call,
                                   @NonNull Response<AllCheckOnResponse> response) {
                dialog.cancel();
                AllCheckOnResponse allCheckOnResponse = response.body();
                Log.d(TAG, "onResponse: " + response.toString());
                assert allCheckOnResponse != null;
                if (allCheckOnResponse.getStatus() == 800) {
                    mCheckOnBeanList.clear();
                    mCheckOnBeanList = allCheckOnResponse.getData();
                    initCheckData();
                } else {
                    Log.d(TAG, "onResponse: 请求错误" + allCheckOnResponse.getMsg()
                            + allCheckOnResponse.getErrorMsg());
                    Toast.makeText(AttendanceActivity.this, "错误，" + allCheckOnResponse.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllCheckOnResponse> call, @NonNull Throwable t) {
                dialog.cancel();
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(AttendanceActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCheckData() {
        mWaitCheckList.clear();
        mFinishList.clear();
        mLeaveList.clear();
        mNoCheckList.clear();
        for (CheckOnBean check : mCheckOnBeanList) {
            switch (check.getCheckOnStates()) {
                case 0: {
                    // 待签到
                    mWaitCheckList.add(check);
                    break;
                }
                case 1: {
                    // 已签
                    mFinishList.add(check);
                    break;
                }
                case 2: {
                    // 请假
                    mLeaveList.add(check);
                    break;
                }
                case 3: {
                    // 过期未签
                    mNoCheckList.add(check);
                    break;
                }
                default: {
                    Log.w(TAG, "initCheckData: CheckOnStates没有此case" + check.getCheckOnStates());
                    break;
                }
            }
        }
        initPieChartView();
        initOtherView();
    }

    /**
     * 设置点击事件
     */
    private void initEvent() {
        SmartRefreshLayout refreshLayout = findViewById(R.id.srl_flush);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(v -> {
            initData();
            v.finishRefresh(true);
        });

        RelativeLayout absence = findViewById(R.id.rl_wait);
        absence.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new AbsenceFragment(mWaitCheckList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View late = findViewById(R.id.rl_finish);
        late.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new NormalFragment(mFinishList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View leave = findViewById(R.id.rl_leave);
        leave.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new LeaveFragment(mLeaveList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View normal = findViewById(R.id.rl_late);
        normal.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new LateFragment(mNoCheckList));
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     * 加载饼图View
     */
    public void initPieChartView() {
        final int waitCheckSize = mWaitCheckList.size();
        final int finishCheckSize = mFinishList.size();
        final int leaveSize = mLeaveList.size();
        final int noCheckSize = mNoCheckList.size();

        mTvWait.setText(waitCheckSize + "次");
        mTvFinish.setText(finishCheckSize + "次");
        mTvLeave.setText(leaveSize + "次");
        mTvLate.setText(noCheckSize + "次");

        List<PieEntry> pieList = new ArrayList<>();
        pieList.add(new PieEntry(waitCheckSize, "待签"));
        pieList.add(new PieEntry(finishCheckSize, "已签"));
        pieList.add(new PieEntry(leaveSize, "请假"));
        pieList.add(new PieEntry(noCheckSize, "过期未签"));

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#6785f2"));
        colors.add(Color.parseColor("#675cf2"));
        colors.add(Color.parseColor("#496cef"));
        colors.add(Color.parseColor("#aa63fa"));

        PieChart pie = findViewById(R.id.pie_chart);
        PieChartManager pieChartManager = new PieChartManager(pie);
        pieChartManager.showSolidPieChart(pieList, colors);
    }

    @SuppressLint("SetTextI18n")
    private void initOtherView() {
        mTvProportion.setText(mFinishList.size() + " / " + mCheckOnBeanList.size());
        if (!mCheckOnBeanList.isEmpty()) {
            mTvName.setText(mCheckOnBeanList.get(0).getStudent().getName());
            Glide.with(this).load(mCheckOnBeanList.get(0).getStudent().getAvatar()).into(mIvAvatar);
        }
    }
}

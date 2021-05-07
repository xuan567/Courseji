package com.littlecorgi.attendance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.littlecorgi.attendance.logic.AttendanceRepository;
import com.littlecorgi.attendance.logic.model.AllCheckOnResponse;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.attendance.tools.PieChartManager;
import com.littlecorgi.commonlib.AppViewModel;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 考勤统计主页面
 *
 * @author littlecorgi 2021/05/07
 */
@Route(path = "/attendance/fragment_attendance")
public class AttendanceFragment extends Fragment {

    private static final String TAG = "AttendanceActivity";

    private AppViewModel mViewModel;
    private long mStudentId = -1L;
    private List<CheckOnBean> mCheckOnBeanList = new ArrayList<>();
    private final List<CheckOnBean> mWaitCheckList = new ArrayList<>();
    private final List<CheckOnBean> mFinishList = new ArrayList<>();
    private final List<CheckOnBean> mLeaveList = new ArrayList<>();
    private final List<CheckOnBean> mNoCheckList = new ArrayList<>();

    private View mView;
    private TextView mTvProportion;
    private TextView mTvWait;
    private TextView mTvFinish;
    private TextView mTvLeave;
    private TextView mTvLate;
    private TextView mTvName;
    private CircleImageView mIvAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // 在此获取bundle传过来的数据
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_attendance, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        Log.d(TAG, "onCreateView: " + mViewModel);

        mStudentId = mViewModel.getStudentId();

        initView();
        initEvent();

        if (mStudentId != -1) {
            initData();
        }
        return mView;
    }

    private void initView() {
        mTvProportion = mView.findViewById(R.id.tv_proportion);
        mTvWait = mView.findViewById(R.id.tv_sum_wait);
        mTvFinish = mView.findViewById(R.id.tv_sum_finish);
        mTvLeave = mView.findViewById(R.id.tv_sum_leave);
        mTvLate = mView.findViewById(R.id.tv_sum_late);
        mTvName = mView.findViewById(R.id.tv_name);
        mIvAvatar = mView.findViewById(R.id.civ_user_icon);
    }

    private void initData() {
        Dialog dialog = DialogUtil.writeLoadingDialog(requireContext(), false, "请求数据中");
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
                    Toast.makeText(requireContext(), "错误，" + allCheckOnResponse.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllCheckOnResponse> call, @NonNull Throwable t) {
                dialog.cancel();
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
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
        SmartRefreshLayout refreshLayout = mView.findViewById(R.id.srl_flush);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(v -> {
            mStudentId = mViewModel.getStudentId();
            initData();
            v.finishRefresh(true);
        });

        RelativeLayout absence = mView.findViewById(R.id.rl_wait);
        absence.setOnClickListener(v -> {
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new AbsenceFragment(mWaitCheckList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View late = mView.findViewById(R.id.rl_finish);
        late.setOnClickListener(v -> {
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new NormalFragment(mFinishList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View leave = mView.findViewById(R.id.rl_leave);
        leave.setOnClickListener(v -> {
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new LeaveFragment(mLeaveList));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        View normal = mView.findViewById(R.id.rl_late);
        normal.setOnClickListener(v -> {
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.layout_attendance, new LateFragment(mNoCheckList));
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     * 加载饼图View
     */
    @SuppressLint("SetTextI18n")
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

        PieChart pie = mView.findViewById(R.id.pie_chart);
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

    /**
     * 获取该fragment的一个新的实例
     */
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
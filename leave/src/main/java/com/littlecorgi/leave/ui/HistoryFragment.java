package com.littlecorgi.leave.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.commonlib.AppViewModel;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.logic.LeaveRepository;
import com.littlecorgi.leave.logic.model.AllLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveBean;
import com.littlecorgi.leave.ui.adapter.LeaveHistoryAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 历史Fragment
 */
public class HistoryFragment extends Fragment {

    private AppViewModel mViewModel;
    private final List<LeaveBean> mLeaveList = new ArrayList<>();
    LeaveHistoryAdapter mAdapter;
    private long studentId;

    private static final String TAG = "HistoryFragment";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_history_leave, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        studentId = mViewModel.getStudentId();

        RecyclerView recyclerView = view.findViewById(R.id.history_leave);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LeaveHistoryAdapter(requireActivity(), mLeaveList);
        recyclerView.setAdapter(mAdapter);

        RefreshLayout refreshLayout = view.findViewById(R.id.history_refresh);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            studentId = mViewModel.getStudentId();
            if (studentId != -1) {
                initHistories();
            }
            refreshLayout.finishRefresh(true);
        });

        if (studentId != -1) {
            initHistories();
        } else {
            Toast.makeText(requireContext(), "未登录或者数据错误", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void initHistories() {
        Dialog loading = DialogUtil.writeLoadingDialog(requireContext(), false, "正在加载中");
        loading.show();
        loading.setCancelable(false);
        LeaveRepository.getLeaveFromStudent(studentId).enqueue(
                new Callback<AllLeaveResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AllLeaveResponse> call,
                                           @NonNull Response<AllLeaveResponse> response) {
                        loading.cancel();
                        Log.d(TAG, "onResponse: " + response.toString());
                        AllLeaveResponse leaveResponse = response.body();
                        assert leaveResponse != null;
                        if (leaveResponse.getStatus() == 800) {
                            mLeaveList.clear();
                            mLeaveList.addAll(leaveResponse.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AllLeaveResponse> call,
                                          @NonNull Throwable t) {
                        loading.cancel();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        t.printStackTrace();
                        Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

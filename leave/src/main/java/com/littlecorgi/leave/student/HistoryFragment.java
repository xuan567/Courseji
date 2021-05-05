package com.littlecorgi.leave.student;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.commonlib.util.UserSPConstant;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.logic.LeaveRepository;
import com.littlecorgi.leave.logic.model.AllLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveBean;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 历史Fragment
 */
public class HistoryFragment extends Fragment {

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
        SharedPreferences sp = requireContext()
                .getSharedPreferences(UserSPConstant.FILE_NAME, Context.MODE_PRIVATE);
        studentId = sp.getLong(UserSPConstant.STUDENT_USER_ID, 1L);
        if (studentId != -1) {
            initHistories();
            RecyclerView recyclerView = view.findViewById(R.id.history_leave);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new LeaveHistoryAdapter(mLeaveList);
            recyclerView.setAdapter(mAdapter);
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

    class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.ViewHolder> {

        private final List<LeaveBean> mLeaveList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mLeaveTitleText;
            TextView mLeavePeopleText;
            TextView mLeaveTimeText;
            TextView mLeaveReasonText;
            TextView mLeaveBackText;
            View mHistoryView;

            public ViewHolder(View view) {
                super(view);
                mHistoryView = view;
                mLeaveTitleText = view.findViewById(R.id.leaveTitleText);
                mLeavePeopleText = view.findViewById(R.id.leavePeopleText);
                mLeaveTimeText = view.findViewById(R.id.leaveTimeText);
                mLeaveReasonText = view.findViewById(R.id.leaveReasonText);
                mLeaveBackText = view.findViewById(R.id.leaveBackText);
            }
        }

        public LeaveHistoryAdapter(List<LeaveBean> leaveList) {
            mLeaveList = leaveList;
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_history_leave_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.mHistoryView.setOnClickListener(v -> {
                PeopleHistoryFragment peopleHistoryFragment = new PeopleHistoryFragment();
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.student_leave, peopleHistoryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
            return holder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull LeaveHistoryAdapter.ViewHolder holder, int position) {
            LeaveBean leaveBean = mLeaveList.get(position);
            holder.mLeaveTitleText.setText(leaveBean.getTitle());
            holder.mLeavePeopleText.setText(leaveBean.getStudent().getName());
            holder.mLeaveTimeText.setText(
                    TimeUtil.INSTANCE.getTimeFromTimestamp(leaveBean.getStartTime())
                            + "至" + TimeUtil.INSTANCE.getTimeFromTimestamp(leaveBean.getEndTime()));
            holder.mLeaveReasonText.setText(leaveBean.getDescription());
            holder.mLeaveBackText.setText((leaveBean.getStates() == 1) ? "审批完成" : "待审批");
        }

        @Override
        public int getItemCount() {
            return mLeaveList.size();
        }
    }
}

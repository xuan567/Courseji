package com.littlecorgi.leave.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.leave.PeopleHistoryActivity;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.logic.model.LeaveBean;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * 请假历史页面RecyclerView的adapter
 *
 * @author littlecorgi 2021/5/5
 */
public class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.ViewHolder> {

    private final List<LeaveBean> mLeaveList;
    private final FragmentActivity mActivity;

    public LeaveHistoryAdapter(FragmentActivity context, List<LeaveBean> leaveList) {
        mLeaveList = leaveList;
        mActivity = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_history_leave_item, parent, false);
        return new ViewHolder(view);
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
        String leaveBackTest;
        if (leaveBean.getStates() == 0) {
            leaveBackTest = "待审批";
        } else if (leaveBean.getStates() == 1) {
            leaveBackTest = "准假";
        } else {
            leaveBackTest = "不准假";
        }
        holder.mLeaveBackText.setText(leaveBackTest);
        holder.mHistoryView.setOnClickListener(v -> {
            mActivity.startActivity(new Intent(mActivity, PeopleHistoryActivity.class)
                    .putExtra("mLeaveId", mLeaveList.get(position).getId()));
        });
    }

    @Override
    public int getItemCount() {
        return mLeaveList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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
}

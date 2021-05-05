package com.littlecorgi.attendance.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.attendance.R;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import java.util.List;

/**
 * 缺勤的RecyclerView的Adapter
 */
public class LeaveFragmentAdapter extends RecyclerView.Adapter<LeaveFragmentAdapter.ViewHolder> {

    private final List<CheckOnBean> mLeaveList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lesson;
        TextView teacher;

        public ViewHolder(View view) {
            super(view);
            lesson = view.findViewById(R.id.leave_lesson);
            teacher = view.findViewById(R.id.leave_teacher);
        }
    }

    public LeaveFragmentAdapter(List<CheckOnBean> leaveList) {
        this.mLeaveList = leaveList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_leave_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckOnBean leave = mLeaveList.get(position);
        holder.lesson.setText(leave.getAttendance().getClassDetail().getName());
        holder.teacher.setText(leave.getAttendance().getClassDetail().getTeacher().getName());
    }

    @Override
    public int getItemCount() {
        return mLeaveList.size();
    }
}

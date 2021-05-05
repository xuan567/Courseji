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
 * 迟到页面的RecyclerView的Adapter
 */
public class LateFragmentAdapter extends RecyclerView.Adapter<LateFragmentAdapter.ViewHolder> {

    private final List<CheckOnBean> mLateList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lessonText;
        TextView teacherText;

        public ViewHolder(View view) {
            super(view);
            lessonText = view.findViewById(R.id.late_lesson);
            teacherText = view.findViewById(R.id.late_teacher);
        }
    }

    public LateFragmentAdapter(List<CheckOnBean> lateList) {
        this.mLateList = lateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_late_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckOnBean late = mLateList.get(position);
        holder.teacherText.setText(late.getAttendance().getClassDetail().getTeacher().getName());
        holder.lessonText.setText(late.getAttendance().getClassDetail().getName());
    }

    @Override
    public int getItemCount() {
        return mLateList.size();
    }
}

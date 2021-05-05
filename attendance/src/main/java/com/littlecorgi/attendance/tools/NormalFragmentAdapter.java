package com.littlecorgi.attendance.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.attendance.R;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.commonlib.util.TimeUtil;
import java.util.List;

/**
 * 已签页面的RecyclerView的Adapter
 */
public class NormalFragmentAdapter extends RecyclerView.Adapter<NormalFragmentAdapter.ViewHolder> {

    private final List<CheckOnBean> mNormalList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lesson;
        TextView teacher;
        TextView time;

        public ViewHolder(View view) {
            super(view);
            lesson = view.findViewById(R.id.normal_lesson);
            teacher = view.findViewById(R.id.normal_teacher);
            time = view.findViewById(R.id.normal_time);
        }
    }

    public NormalFragmentAdapter(List<CheckOnBean> normals) {
        this.mNormalList = normals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_normal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckOnBean normal = mNormalList.get(position);
        holder.lesson.setText(normal.getAttendance().getClassDetail().getName());
        holder.teacher.setText(normal.getAttendance().getClassDetail().getTeacher().getName());
        holder.time.setText(TimeUtil.INSTANCE.getTimeFromTimestamp(normal.getLastModifiedTime()));
    }

    @Override
    public int getItemCount() {
        return mNormalList.size();
    }
}

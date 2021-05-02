package com.littlecorgi.leave.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.leave.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * 历史Fragment
 */
public class HistoryFragment extends Fragment {

    private final boolean mIsGetData = false;
    private final List<History> mHistoryList = new ArrayList<>();
    LeaveHistoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_history_leave, container, false);
        File f = new File("/data/data/com.example.leave/shared_prefs/data.xml");
        if (f.exists()) {
            initHistories();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.history_leave);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new LeaveHistoryAdapter(mHistoryList);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    private void initHistories() {
        SharedPreferences pref =
                requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = pref.getString("name", "");
        String type1 = pref.getString("type1", "");
        String startTime = pref.getString("startTime", "");
        String endTime = pref.getString("endTime", "");
        String leaveSituation = pref.getString("leaveSituation", "");
        History history = new History(type1, name, startTime + "至" + endTime, leaveSituation,
                "未销假");
        mHistoryList.add(history);
    }

    class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.ViewHolder> {

        private final List<History> mHistoryList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mLeaveTypeText;
            TextView mLeavePeopleText;
            TextView mLeaveTimeText;
            TextView mLeaveReasonText;
            TextView mLeaveBackText;
            View mHistoryView;

            public ViewHolder(View view) {
                super(view);
                mHistoryView = view;
                mLeaveTypeText = (TextView) view.findViewById(R.id.leaveTypeText);
                mLeavePeopleText = (TextView) view.findViewById(R.id.leavePeopleText);
                mLeaveTimeText = (TextView) view.findViewById(R.id.leaveTimeText);
                mLeaveReasonText = (TextView) view.findViewById(R.id.leaveReasonText);
                mLeaveBackText = (TextView) view.findViewById(R.id.leaveBackText);
            }
        }

        public LeaveHistoryAdapter(List<History> historyList) {
            mHistoryList = historyList;
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

        @Override
        public void onBindViewHolder(@NonNull LeaveHistoryAdapter.ViewHolder holder, int position) {
            History history = mHistoryList.get(position);
            holder.mLeaveTypeText.setText(history.getLeaveTypeText());
            holder.mLeavePeopleText.setText(history.getLeavePeopleText());
            holder.mLeaveTimeText.setText(history.getLeaveTimeText());
            holder.mLeaveReasonText.setText(history.getLeaveReasonText());
            holder.mLeaveBackText.setText(history.getLeaveBackText());
        }

        @Override
        public int getItemCount() {
            return mHistoryList.size();
        }
    }
}

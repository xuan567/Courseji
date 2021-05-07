package com.littlecorgi.attendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.attendance.tools.AbsenceFragmentAdapter;
import java.util.List;

/**
 * 考勤统计Fragment
 */
public class AbsenceFragment extends Fragment {

    private final List<CheckOnBean> mAbsenceLists;

    public AbsenceFragment(List<CheckOnBean> absenceLists) {
        this.mAbsenceLists = absenceLists;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_absence, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.absence_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        AbsenceFragmentAdapter adapter = new AbsenceFragmentAdapter(mAbsenceLists);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = view.findViewById(R.id.layout_absence_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.popBackStack();
        });
        return view;
    }
}

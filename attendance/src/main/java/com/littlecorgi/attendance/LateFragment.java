package com.littlecorgi.attendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.attendance.tools.LateFragmentAdapter;
import java.util.List;

/**
 * 迟到的Fragment
 */
public class LateFragment extends Fragment {

    private final List<CheckOnBean> mLateList;

    public LateFragment(List<CheckOnBean> lateList) {
        this.mLateList = lateList;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_late, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.late_recycler);
        LateFragmentAdapter adapter = new LateFragmentAdapter(mLateList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Button returnButton = view.findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> {
            FragmentManager manager1 = requireActivity().getSupportFragmentManager();
            manager1.popBackStack();
        });
        return view;
    }
}

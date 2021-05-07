package com.littlecorgi.attendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.attendance.logic.model.CheckOnBean;
import com.littlecorgi.attendance.tools.NormalFragmentAdapter;
import java.util.List;

/**
 * 已签的Fragment
 */
public class NormalFragment extends Fragment {

    private final List<CheckOnBean> mNormalList;
    private Button mReturnButton;

    public NormalFragment(List<CheckOnBean> normalList) {
        this.mNormalList = normalList;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_normal, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.normal_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        NormalFragmentAdapter adapter = new NormalFragmentAdapter(mNormalList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = view.findViewById(R.id.layout_normal_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager manager1 = requireActivity().getSupportFragmentManager();
            manager1.popBackStack();
        });
        return view;
    }
}

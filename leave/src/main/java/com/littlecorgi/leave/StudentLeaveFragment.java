package com.littlecorgi.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.littlecorgi.leave.ui.AskLeaveFragment;
import com.littlecorgi.leave.ui.HistoryFragment;
import com.littlecorgi.leave.ui.adapter.PagerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 学生请假页面的Fragment
 *
 * @author littlecorgi 2021/05/07
 */
@Route(path = "/leave/fragment_student_leave")
public class StudentLeaveFragment extends Fragment {

    public TabLayout tabLayout;
    List<Fragment> fragments = new ArrayList<>();
    private View mView;

    public StudentLeaveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // 如果有bundle的数据在此获取
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_student_leave, container, false);
        initView();
        return mView;
    }

    private void initView() {
        initTabLayout();
    }

    private void initTabLayout() {
        tabLayout = mView.findViewById(R.id.tab_layout);

        fragments.add(new AskLeaveFragment());
        fragments.add(new HistoryFragment());

        ViewPager viewPager = mView.findViewById(R.id.leave_viewpager);
        PagerAdapter adapter = new PagerAdapter(getParentFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        String[] titles = new String[] {"提交请假", "请假记录"};
        for (int i = 0; i < titles.length; i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setText(titles[i]);
        }
    }

    /**
     * 创建该Fragment的实例
     */
    public static StudentLeaveFragment newInstance() {
        StudentLeaveFragment fragment = new StudentLeaveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
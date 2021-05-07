package com.littlecorgi.leave.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * ViewPager的Adapter
 */
public class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    // 根据Item的位置返回对应位置的Fragment，绑定item和Fragment
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    // 设置Item的数量
    @Override
    public int getCount() {
        return fragments.size();
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}

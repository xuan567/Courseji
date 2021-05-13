package com.littlecorgi.leave;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import com.littlecorgi.leave.databinding.PhotoViewBinding;
import com.littlecorgi.leave.ui.adapter.MyImageAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 查看图片页面
 */
public class PhotoViewActivity extends AppCompatActivity {

    ViewPager mViewpager;
    TextView mTvImageCount;

    private List<String> urlLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBinding
        PhotoViewBinding binding = DataBindingUtil.setContentView(this, R.layout.photo_view);
        mViewpager = binding.viewPager;
        mTvImageCount = binding.mTvImageCount;
        initData();
    }

    private void initData() {
        if (null == urlLists) {
            urlLists = new ArrayList<>();
        }
        // 获得点击的位置
        // 点击的下标
        int currentPosition = getIntent().getIntExtra("position", 0);
        // 图片集合
        urlLists = getIntent().getStringArrayListExtra("list");
        MyImageAdapter adapter = new MyImageAdapter(this, urlLists);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(currentPosition);
        String text = currentPosition + 1 + "/" + urlLists.size();
        mTvImageCount.setText(text);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String text = position + 1 + "/" + urlLists.size();
                mTvImageCount.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}

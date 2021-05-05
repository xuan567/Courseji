package com.littlecorgi.leave.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.littlecorgi.leave.ui.util.Tools;
import com.luck.picture.lib.photoview.PhotoView;
import java.util.List;

/**
 * 图片加载Adapter
 */
public class MyImageAdapter extends PagerAdapter {

    private final List<String> mImageUrls;
    private final Activity mContext;

    public MyImageAdapter(Activity context, List<String> imageUrls) {
        this.mImageUrls = imageUrls;
        this.mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String url = mImageUrls.get(position);
        PhotoView photoView = new PhotoView(mContext);
        Tools.showGlide(mContext, photoView, url);
        container.addView(photoView);
        photoView.setOnClickListener(v -> mContext.finish());
        return photoView;
    }

    @Override
    public int getCount() {
        return mImageUrls != null ? mImageUrls.size() : 0;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

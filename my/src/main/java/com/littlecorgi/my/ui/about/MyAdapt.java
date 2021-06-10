package com.littlecorgi.my.ui.about;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.littlecorgi.my.R;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * RecyclerView的Adapter
 */
public class MyAdapt extends BaseQuickAdapter<String, BaseViewHolder> {

    private final Context context;

    public MyAdapt(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        AppCompatImageView imageView = baseViewHolder.getView(R.id.my_im_show_gallery);
        Glide.with(context).load(s).into(imageView);
    }
}

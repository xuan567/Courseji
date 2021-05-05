package com.littlecorgi.leave.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.ui.util.Tools;
import java.util.List;

/**
 * 相册选择Adapter
 */
public class SelectPlotAdapter extends RecyclerView.Adapter<SelectPlotAdapter.SelectHolder> {

    private final Context mContext;
    private List<String> mediaDtoList;
    // 最大图片张数
    private final int mPicMax;

    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    private CallbackListener listener;

    /**
     * 回调接口
     */
    public interface CallbackListener {

        void add();

        void delete(int position);

        void item(int position, List<String> list);
    }

    /**
     * 选择的Holder
     */
    public static class SelectHolder extends RecyclerView.ViewHolder {

        private final ImageView mGallery;
        private final ImageView mDelete;

        /**
         * 构造方法
         *
         * @param itemView 父View对象
         */
        public SelectHolder(View itemView) {
            super(itemView);
            mGallery = itemView.findViewById(R.id.im_show_gallery);
            mDelete = itemView.findViewById(R.id.iv_del);
        }
    }

    public SelectPlotAdapter(Context context, int max) {
        this.mContext = context;
        this.mPicMax = max;
    }

    public void setImageList(List<String> list) {
        this.mediaDtoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectHolder holder, int position) {
        // 当前位置大于图片数量并且小于最大减一
        if (position >= mediaDtoList.size() && position <= (mPicMax - 1)) {
            // 显示添加图片按钮，并显示删除按钮
            Tools.showGlide(mContext, holder.mGallery, "");
            holder.mGallery.setVisibility(View.GONE);
        } else {
            // 显示本地或网络图片，并显示删除按钮
            Tools.showGlide(mContext, holder.mGallery, mediaDtoList.get(position));
            holder.mDelete.setVisibility(View.VISIBLE);
        }
        // 按钮删除事件
        holder.mDelete.setOnClickListener(
                v -> {
                    // 传入position删除第几张
                    listener.delete(position);
                });
        holder.mGallery.setOnClickListener(
                v -> {
                    // 添加新图片点击事件（回调activity）
                    if (position >= mediaDtoList.size() && position <= (mPicMax - 1)) {
                        listener.add();
                    } else {
                        // 点击查看图片事件，并将最新list传入activity
                        listener.item(position, mediaDtoList);
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mediaDtoList == null || mediaDtoList.size() == 0) {
            return 1;
        } else {
            return this.mediaDtoList.size() >= mPicMax ? mPicMax : this.mediaDtoList.size() + 1;
        }
    }
}

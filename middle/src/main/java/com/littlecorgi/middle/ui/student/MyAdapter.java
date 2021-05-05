package com.littlecorgi.middle.ui.student;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.middle.R;
import com.littlecorgi.middle.logic.model.ItemData;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 签到信息RecyclerView的Adapter
 */
public class MyAdapter extends BaseQuickAdapter<ItemData.AllSignData, BaseViewHolder> {

    public MyAdapter(int layoutResId, @Nullable List<ItemData.AllSignData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ItemData.AllSignData itemData) {
        baseViewHolder
                .setText(R.id.middle_item_theme, itemData.getTheme())
                .setText(R.id.middle_item_stateButton, itemData.getStateTitle())
                .setText(R.id.middle_item_labelTextView, itemData.getLabelTitle())
                .setText(R.id.middle_item_timeTextView,
                        TimeUtil.INSTANCE.getTimeFromTimestamp(itemData.getStartTime())
                                + "至"
                                + TimeUtil.INSTANCE.getTimeFromTimestamp(itemData.getEndTime()))
                .setBackgroundColor(R.id.middle_item_viewColor, itemData.getLeftColor())
                .setBackgroundColor(R.id.middle_item_stateButton, itemData.getLeftColor());
    }
}

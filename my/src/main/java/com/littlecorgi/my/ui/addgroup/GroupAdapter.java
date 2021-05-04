package com.littlecorgi.my.ui.addgroup;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.littlecorgi.my.R;
import com.littlecorgi.my.logic.model.GroupNameAndTeacher;
import java.util.List;

/**
 * @author littlecorgi 2021/5/4
 */
class GroupAdapter extends BaseQuickAdapter<GroupNameAndTeacher, BaseViewHolder> {
    public GroupAdapter(List data) {
        super(R.layout.item_rv_group, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper,
                           GroupNameAndTeacher item) {
        helper.setText(R.id.item_group_class_name, item.getName());
        helper.setText(R.id.item_group_teacher_name, item.getTeacherName());
    }
}

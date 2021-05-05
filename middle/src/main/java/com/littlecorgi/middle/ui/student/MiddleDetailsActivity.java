package com.littlecorgi.middle.ui.student;

import static com.littlecorgi.middle.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.middle.R;
import com.littlecorgi.middle.logic.model.Details;

/**
 * 详情Activity
 */
public class MiddleDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.middle_details);
        initView();
        initData();
        initClick();
    }

    private void initView() {
        changeBarColor();
    }

    private void changeBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initClick() {
        AppCompatTextView returnButton = findViewById(R.id.middle_sign_returnButton);
        returnButton.setOnClickListener(v -> finish());
    }

    private void initData() {
        final AppCompatImageView imageView = findViewById(R.id.middle_sign_Image);
        final AppCompatTextView name = findViewById(R.id.middle_sign_name);
        final AppCompatTextView occupational = findViewById(R.id.middle_sign_occupational);
        final AppCompatTextView label = findViewById(R.id.middle_sign_label);
        final AppCompatTextView startTime = findViewById(R.id.middle_sign_startTime);
        final AppCompatTextView endTime = findViewById(R.id.middle_sign_endTime);
        final AppCompatTextView theme = findViewById(R.id.middle_sign_theme);
        final AppCompatTextView title = findViewById(R.id.middle_sign_title);

        Intent intent = getIntent();
        Details details = (Details) intent.getSerializableExtra("details");
        assert details != null;

        Glide.with(this).load(details.getImage()).into(imageView); // Glide加载图片
        name.setText(details.getName());
        occupational.setText("教师");
        label.setText(details.getLabel());
        startTime.setText(TimeUtil.INSTANCE.getTimeFromTimestamp(details.getStartTime()));
        endTime.setText(TimeUtil.INSTANCE.getTimeFromTimestamp(details.getEndTime()));
        theme.setText(details.getTheme());
        title.setText(details.getTitle());
    }

    /**
     * 跳转到详情页
     *
     * @param context 上下文
     * @param details 详情信息
     */
    public static void startDetails(Context context, Details details) {
        Intent intent = new Intent(context, MiddleDetailsActivity.class);
        intent.putExtra("details", details);
        context.startActivity(intent);
    }
}

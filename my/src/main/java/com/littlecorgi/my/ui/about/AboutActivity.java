package com.littlecorgi.my.ui.about;

import static com.littlecorgi.my.logic.dao.Tool.copyHelper;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;
import static com.littlecorgi.my.ui.about.AdviceActivity.startAdviceActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.my.BuildConfig;
import com.littlecorgi.my.R;

/**
 * 关于页面
 */
public class AboutActivity extends BaseActivity {

    /*
       这里需要版本号和下载地址，这里的处理为从Message中获取，Message为intent传入的带有数据的对象
    */
    private AppCompatTextView update;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_about);
        initView();
        initData();
        // 点击事件
        initClick();
    }

    private void initView() {
        initBarColor();
        toolbar = findViewById(R.id.topLayout);
        /*
        设置背景？
         */
        // AppCompatImageView imageView = findViewById(R.id.my_about_bg);
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initData() {
        AppCompatTextView textView = findViewById(R.id.versionNumber);
        textView.setText(BuildConfig.VERSION_NAME);
        update = findViewById(R.id.my_about_update);
        update.setText("https://github.com/xuan567/Courseji");
    }

    private void initClick() {
        toolbar.setNavigationOnClickListener(v -> finish());
        ConstraintLayout adviceLayout = findViewById(R.id.my_about_advice);
        adviceLayout.setOnClickListener(v -> startAdviceActivity(this));
        // 长按复制
        update.setOnLongClickListener(v -> {
            copyHelper(this, update.getText().toString());
            return false;
        });
    }

    /**
     * 跳转到AboutActivity
     *
     * @param context 上下文
     */
    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}

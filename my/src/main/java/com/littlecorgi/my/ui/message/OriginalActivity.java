package com.littlecorgi.my.ui.message;

import static com.littlecorgi.my.logic.dao.BTWHelp.dialogBtw;
import static com.littlecorgi.my.logic.dao.SavePhotoHelp.saveBitmapToGallery;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.my.R;

/**
 * 图片查看Activity
 */
public class OriginalActivity extends BaseActivity {

    private AppCompatImageView mDisplayImage;
    private View mEmptyView1;
    private View mEmptyView2;
    private String mPath;
    private Dialog mDialog;
    private View mDisplayBtw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_picuure_display);
        initView();
        initData();
        initClick();
    }

    private void initClick() {
        mDisplayImage.setOnLongClickListener(v -> {
            showBtw();
            return true;
        });
        mDisplayImage.setOnClickListener(v -> finish());
        mEmptyView1.setOnClickListener(v -> finish());
        mEmptyView2.setOnClickListener(v -> finish());
    }

    private void showBtw() {
        if (mDialog != null) {
            mDialog.show();
        } else {
            mDialog = dialogBtw(mDisplayBtw, this);
        }
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initView() {
        initBarColor();
        mDisplayImage = findViewById(R.id.my_display_image);
        mEmptyView1 = findViewById(R.id.display_emptyView1);
        mEmptyView2 = findViewById(R.id.display_emptyView2);
        mDisplayBtw = View.inflate(this, R.layout.my_display_btw, null);
        AppCompatTextView save = mDisplayBtw.findViewById(R.id.display_btw_save);
        AppCompatTextView cancel = mDisplayBtw.findViewById(R.id.display_btw_cancel);
        save.setOnClickListener(v -> {
            saveImage();
            mDialog.dismiss();
        });
        cancel.setOnClickListener(v -> mDialog.dismiss());
    }

    private void saveImage() {
        /*
        把图片保存到本地
         */
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        // 使用bitmap.hashCode()作为文件名来保存图片
        saveBitmapToGallery(this, bitmap.hashCode() + "", bitmap);
        Toast.makeText(this, "图片已保存", Toast.LENGTH_LONG).show();
    }

    private void initData() {
        Intent intent = getIntent();
        mPath = intent.getStringExtra("imagePath");
        Glide.with(this).load(mPath).into(mDisplayImage);
    }

    /**
     * 跳转到OriginalActivity
     *
     * @param context   上下文
     * @param imagePath 图片路径
     */
    public static void startOriginalActivity(Context context, String imagePath) {
        Intent intent = new Intent(context, OriginalActivity.class);
        intent.putExtra("imagePath", imagePath);
        context.startActivity(intent);
    }
}

package com.littlecorgi.my.ui.about;

import static com.littlecorgi.my.logic.dao.AndPermissionHelp.andPermission;
import static com.littlecorgi.my.logic.dao.BTWHelp.dialogBtw;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.choicePhoto;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.choiceVideo;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.my.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yanzhenjie.permission.runtime.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AdviceActivity extends BaseActivity implements View.OnClickListener {

    // todo: 未完成：sendAdvice()把建议上传到服务器，需要修改路径

    private static final int PHOTO = 1;
    private AppCompatEditText mEditText;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private ArrayList<String> mAllSelectList; // 所有的图片集合
    private final List<LocalMedia> mSelectList = new ArrayList<>();
    private MyAdapt mAdapt;
    private Dialog mDialog;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_about_advice);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.advice_toolbar);
        setSupportActionBar(mToolbar);
        mEditText = findViewById(R.id.my_advice_editView);
        mRecyclerView = findViewById(R.id.my_advice_RecyclerView);
        AppCompatTextView getPicture = findViewById(R.id.my_advice_getPicture);
        AppCompatButton sureButton = findViewById(R.id.my_advice_SureButton);
        mToolbar.setNavigationOnClickListener(v -> finish());
        getPicture.setOnClickListener(this);
        sureButton.setOnClickListener(this);
        initBarColor();
        initPermission();
        initRecyclerView();
        initBtw();
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initBtw() {
        mView = View.inflate(this, R.layout.my_advice_btw, null);
        // 点击事件：
        AppCompatTextView photo = mView.findViewById(R.id.advice_btw_photo);
        AppCompatTextView video = mView.findViewById(R.id.advice_btw_video);
        AppCompatTextView cancel = mView.findViewById(R.id.picture_btw_cancel);
        photo.setOnClickListener(v -> uploadPhoto());
        video.setOnClickListener(v -> uploadVideo());
        cancel.setOnClickListener(v -> mDialog.dismiss());
    }

    /**
     * 使用AndPermission获取动态权限
     */
    private void initPermission() {
        andPermission(this, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initRecyclerView() {
        mAllSelectList = new ArrayList<>();
        mAdapt = new MyAdapt(R.layout.my_advice_item_gallery, mAllSelectList, AdviceActivity.this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapt);

        mAdapt.addChildClickViewIds(R.id.my_im_show_gallery, R.id.my_im_del);

        mAdapt.setOnItemChildClickListener((adapter, view, position) -> {
            if (mAllSelectList.size() > 0) {
                if (view.getId() == R.id.my_im_show_gallery) {
                    PictureSelector.create(AdviceActivity.this)
                            .externalPicturePreview(position, mSelectList, 0);

                } else if (view.getId() == R.id.my_im_del) {
                    mAllSelectList.remove(position);
                    mSelectList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        // 此处不能使用switch，因为作为library，他的id并不是常量，所以Android并不建议在switch中直接使用id，
        // 可以使用view.setOnClickListener替换，或者使用if-else
        int id = v.getId();
        if (id == R.id.my_advice_getPicture) {
            showBtw();
        } else if (id == R.id.my_advice_SureButton) {
            sendAdvice();
        }
    }

    private void sendAdvice() {
        // String title = Objects.requireNonNull(mEditText.getText()).toString();
        // if (title.equals("")) {
        //     Toast.makeText(AdviceActivity.this, "输入不能为空", Toast.LENGTH_LONG).show();
        // } else {
        //     Map<String, Object> map = new HashMap<>();
        //     map.put("advice_title", title);
        //     map.put("advice_imagePath", mAllSelectList);
        //     Call<ResponseBody> call = adviceRetrofit(map);
        //     call.enqueue(new Callback<ResponseBody>() {
        //         @Override
        //         public void onResponse(
        //                 @NotNull Call<ResponseBody> call,
        //                 @NotNull Response<ResponseBody> response) {
        //             AlertDialog.Builder dialog = new AlertDialog.Builder(
        //                     AdviceActivity.this);
        //             dialog.setMessage("您的建议我们已经收到了！"); // 设置内容
        //             dialog.setCancelable(true); // 设置不可用Back键关闭对话框
        //             // 设置确定按钮的点击事件
        //             dialog.setPositiveButton("退出", (dialogInterface, i) -> finish());
        //             // 设置取消按钮的点击事件
        //             dialog.setNegativeButton(
        //                     "再写一个",
        //                     (dialogInterface, i) -> {
        //                         // 清除缓存文件
        //                         PictureFileUtils.deleteAllCacheDirFile(AdviceActivity.this);
        //                         mAllSelectList.clear();
        //                         mEditText.setText("");
        //                         mAdapt.notifyDataSetChanged();
        //                     });
        //             dialog.show();
        //         }
        //
        //         @Override
        //         public void onFailure(@NotNull Call<ResponseBody> call,
        //                               @NotNull Throwable t) {
        //             Toast.makeText(AdviceActivity.this, "", Toast.LENGTH_LONG).show();
        //         }
        //     });
        // }
    }

    private void showBtw() {
        // 使用Dialog弹出
        if (mDialog == null) {
            mDialog = dialogBtw(mView, this);
        } else {
            mDialog.show();
        }
    }

    private void uploadPhoto() {
        choicePhoto(AdviceActivity.this, PHOTO);
        mDialog.dismiss(); // 关闭下方的弹窗
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO) {
                List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                mSelectList.addAll(localMedia);
                showSelectPic(localMedia);
            }
        }
    }

    private void showSelectPic(List<LocalMedia> localMedia) {
        for (int i = 0; i < localMedia.size(); i++) {
            String path = null;
            // 判断是视频还是图片：视频的获取路径不需要判断是否是10.0以上，否则报错
            int type = localMedia.get(i).getChooseModel();
            if (type == PictureConfig.TYPE_IMAGE) {
                // 判断是否10.0以上
                if (Build.VERSION.SDK_INT >= 29) {
                    path = localMedia.get(i).getAndroidQToPath();
                } else {
                    path = localMedia.get(i).getPath();
                }
            } else if (type == PictureConfig.TYPE_VIDEO) {
                path = localMedia.get(i).getPath();
            }
            if (path != null) {
                mAllSelectList.add(path);
            }
        }
        mAdapt.notifyDataSetChanged();
    }

    private void uploadVideo() {
        choiceVideo(AdviceActivity.this, PHOTO);
        mDialog.dismiss(); // 关闭下方的弹窗
    }

    public static void startAdviceActivity(Context context) {
        Intent intent = new Intent(context, AdviceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除缓存文件
        PictureFileUtils.deleteAllCacheDirFile(AdviceActivity.this);
    }
}

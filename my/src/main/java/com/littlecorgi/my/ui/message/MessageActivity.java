package com.littlecorgi.my.ui.message;

import static com.littlecorgi.my.logic.dao.BTWHelp.dialogBtw;
import static com.littlecorgi.my.logic.dao.PickerViewHelp.getNationalList;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.OPEN_ALBUM;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.OPEN_CAMERA;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.openAlbum;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.openCamera;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;
import static com.littlecorgi.my.logic.network.RetrofitHelp.messageRetrofit;
import static com.littlecorgi.my.ui.message.DescribeActivity.REQUEST_CODE;
import static com.littlecorgi.my.ui.message.DescribeActivity.REQUEST_DATA;
import static com.littlecorgi.my.ui.message.DescribeActivity.startDescribeActivity;
import static com.littlecorgi.my.ui.message.OriginalActivity.startOriginalActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bumptech.glide.Glide;
import com.contrarywind.view.WheelView;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.my.R;
import com.littlecorgi.my.logic.model.MessageChange;
import com.littlecorgi.my.logic.model.MyMessage;
import com.littlecorgi.my.logic.model.Student;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息页面
 */
public class MessageActivity extends BaseActivity {

    private AppCompatTextView mReturnButton;
    private AppCompatButton mSureButton;
    private ConstraintLayout mPictureLayout;
    private AppCompatImageView mPictureView;
    private ConstraintLayout mNameLayout;
    private AppCompatTextView mNameTitle;

    private ConstraintLayout mIdLayout;
    private AppCompatTextView mIdTitle;

    private ConstraintLayout mGenderLayout;
    private AppCompatTextView mGenderTitle;
    private AppCompatEditText mProfessionalTitle;
    private ConstraintLayout mDescribeLayout;
    private AppCompatTextView mDescribeTitle;

    private ConstraintLayout mNationalLayout;
    private AppCompatTextView mNationalTitle;

    private MyMessage mMyMessage;
    private MessageChange mMessageChange;

    private Dialog mPictureDialog;
    private Dialog mGenderDialog;
    private Dialog mNationalDialog;
    private boolean mIsChanged = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                assert data != null;
                String describeData = data.getStringExtra(REQUEST_DATA);
                mDescribeTitle.setText(describeData);
                mMessageChange.setDescribe(describeData);
                mIsChanged = true;
            }
            if (requestCode == OPEN_CAMERA || requestCode == OPEN_ALBUM) {
                String path = getImagePath(data);
                Glide.with(this).load(path).into(mPictureView);
                mMessageChange.setMyImagePath(path);
            }
        }
    }

    private String getImagePath(Intent data) {
        List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
        if (Build.VERSION.SDK_INT >= 29) {
            return localMedia.get(0).getAndroidQToPath();
        } else {
            return localMedia.get(0).getPath();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_message);
        // 初始化view
        initView();
        // 初始化data
        initData();
    }

    private void initView() {
        initBarColor();
        initFind();
        initImageView();
        initNameView();
        initIdVIew();
        initGenderView();
        initDescribeView();
        initNationalView();
        initClick();
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initClick() {
        mReturnButton.setOnClickListener(v -> finish());
        mSureButton.setOnClickListener(v -> saveMessage());
    }

    private void saveMessage() {
        if (!Objects.requireNonNull(mProfessionalTitle.getText())
                .toString()
                .equals(mMyMessage.getProfessional())) {
            mIsChanged = true;
        }
        if (mIsChanged) {
            mMessageChange.setProfessional(mProfessionalTitle.getText().toString());
            Map<String, Object> map = new HashMap<>();
            map.put("image", mMessageChange.getMyImagePath());
            map.put("gender", mMessageChange.getGender());
            map.put("professional", mMessageChange.getProfessional());
            map.put("describe", mMessageChange.getDescribe());
            map.put("national", mMessageChange.getNational());
            Call<ResponseBody> call = messageRetrofit(map);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(
                        @NotNull Call<ResponseBody> call,
                        @NotNull Response<ResponseBody> response) {
                    Toast.makeText(MessageActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    mIsChanged = false;
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call,
                                      @NotNull Throwable t) {
                    Toast.makeText(MessageActivity.this, "保存失败，过会在试吧", Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    private void initNationalView() {
        View nationalBtw = View.inflate(this, R.layout.my_national_btw, null);
        WheelView wheelView = nationalBtw.findViewById(R.id.national_blw_wheelView);
        List<String> list = getNationalList();
        wheelView.setAdapter(new ArrayWheelAdapter<>(list));
        wheelView.setCurrentItem(0);
        mNationalLayout.setOnClickListener(v -> {
            if (mNationalDialog != null) {
                mNationalDialog.show();
            } else {
                mNationalDialog = dialogBtw(nationalBtw, this);
            }
        });
        AppCompatButton cancel = nationalBtw.findViewById(R.id.national_blw_cancelButton);
        AppCompatButton sure = nationalBtw.findViewById(R.id.national_blw_sureButton);
        cancel.setOnClickListener(v -> mNationalDialog.dismiss());
        sure.setOnClickListener(v -> {
            mNationalTitle.setText(list.get(wheelView.getCurrentItem()));
            mMessageChange.setNational(list.get(wheelView.getCurrentItem()));
            mIsChanged = true;
            mNationalDialog.dismiss();
        });
    }

    private void initDescribeView() {
        mDescribeLayout.setOnClickListener(v -> startDescribeActivity(this));
    }

    private void initFind() {
        mReturnButton = findViewById(R.id.my_message_returnButton);
        mSureButton = findViewById(R.id.my_message_SureButton);

        mPictureLayout = findViewById(R.id.my_message_picture);
        mPictureView = findViewById(R.id.my_message_ImageView);

        mNameLayout = findViewById(R.id.my_message_name);
        mNameTitle = findViewById(R.id.my_message_nameTitle);

        mIdLayout = findViewById(R.id.my_message_id);
        mIdTitle = findViewById(R.id.my_message_idTitle);

        mGenderLayout = findViewById(R.id.my_message_gender);
        mGenderTitle = findViewById(R.id.my_message_genderTitle);

        mProfessionalTitle = findViewById(R.id.my_message_professionalTitle);

        mDescribeLayout = findViewById(R.id.my_message_Describe);
        mDescribeTitle = findViewById(R.id.my_message_DescribeTitle);

        mNationalLayout = findViewById(R.id.my_message_national);
        mNationalTitle = findViewById(R.id.my_message_nationalTitle);
    }

    private void initGenderView() {
        View genderBtw = View.inflate(this, R.layout.my_gender_btw, null);
        mGenderLayout.setOnClickListener(v -> {
            if (mGenderDialog != null) {
                mGenderDialog.show();
            } else {
                mGenderDialog = dialogBtw(genderBtw, this);
            }
        });
        AppCompatTextView man = genderBtw.findViewById(R.id.gender_btw_man);
        AppCompatTextView woman = genderBtw.findViewById(R.id.gender_btw_woman);
        AppCompatTextView cancel = genderBtw.findViewById(R.id.gender_btw_cancel);
        man.setOnClickListener(v -> {
            mGenderTitle.setText("男");
            mMessageChange.setGender("男");
            mIsChanged = true;
            mGenderDialog.dismiss();
        });
        woman.setOnClickListener(v -> {
                    mGenderTitle.setText("女");
                    mMessageChange.setGender("女");
                    mIsChanged = true;
                    mGenderDialog.dismiss();
                }
        );
        cancel.setOnClickListener(v -> mGenderDialog.dismiss());
    }

    private void initIdVIew() {
        mIdLayout.setOnClickListener(v ->
                Toast.makeText(MessageActivity.this, "不可修改", Toast.LENGTH_LONG).show());
    }

    private void initNameView() {
        mNameLayout.setOnClickListener(v ->
                Toast.makeText(MessageActivity.this, "不可修改", Toast.LENGTH_LONG).show());
    }

    private void initImageView() {
        View imageBtw = View.inflate(this, R.layout.my_picture_btw, null);
        final AppCompatTextView photo = imageBtw.findViewById(R.id.picture_btw_photo);
        final AppCompatTextView album = imageBtw.findViewById(R.id.picture_btw_album);
        final AppCompatTextView original = imageBtw.findViewById(R.id.picture_btw_original);
        final AppCompatTextView cancel = imageBtw.findViewById(R.id.picture_btw_cancel);
        photo.setOnClickListener(v1 -> photoHelp());
        album.setOnClickListener(v1 -> albumHelp());
        original.setOnClickListener(v1 -> originalHelp());
        cancel.setOnClickListener(v1 -> mPictureDialog.dismiss());

        mPictureLayout.setOnClickListener(v -> {
            if (mPictureDialog != null) {
                mPictureDialog.show();
            } else {
                mPictureDialog = dialogBtw(imageBtw, this);
            }
        });
    }

    private void albumHelp() {
        /*
        打开相册
         */
        openAlbum(this, OPEN_ALBUM);
        mPictureDialog.dismiss();
    }

    private void photoHelp() {
        /*
        拍照
         */
        openCamera(this, OPEN_CAMERA);
        mPictureDialog.dismiss();
    }

    private void originalHelp() {
        /*
        查看大图
         */
        if (mMessageChange.getMyImagePath() == null) {
            startOriginalActivity(this, mMyMessage.getImagePath());
        } else {
            startOriginalActivity(this, mMessageChange.getMyImagePath());
        }
        mPictureDialog.dismiss();
    }

    private void initData() {
        Intent intent = getIntent();
        mMyMessage = (MyMessage) intent.getSerializableExtra("studentInfo");
        assert mMyMessage != null;
        // pictureView.setImageResource(myMessage.getMyImage());
        Glide.with(this).load(mMyMessage.getImagePath()).into(mPictureView);
        mNameTitle.setText(mMyMessage.getName());
        mIdTitle.setText(mMyMessage.getId());
        mGenderTitle.setText(mMyMessage.getGender());
        mProfessionalTitle.setText(mMyMessage.getProfessional());
        mDescribeTitle.setText(mMyMessage.getDescribe());
        mNationalTitle.setText(mMyMessage.getNational());
        mMessageChange = new MessageChange();
    }

    /**
     * 跳转到MessageActivity
     *
     * @param context 上下文
     * @param student 我的信息
     */
    public static void startMessageActivity(Context context, Student student) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("studentInfo", student);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除所有缓存 例如：压缩、裁剪、视频、音频所生成的临时文件
        PictureFileUtils.deleteAllCacheDirFile(this);
    }
}

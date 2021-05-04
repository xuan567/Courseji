package com.littlecorgi.my.ui.signup;

import static com.littlecorgi.my.logic.dao.BTWHelp.dialogBtw;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.OPEN_ALBUM;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.OPEN_CAMERA;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.openAlbum;
import static com.littlecorgi.my.logic.dao.PictureSelectorHelp.openCamera;
import static com.littlecorgi.my.logic.dao.WindowHelp.setWindowStatusBarColor;
import static com.littlecorgi.my.ui.message.OriginalActivity.startOriginalActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.my.R;
import com.littlecorgi.my.databinding.ActivitySignUpBinding;
import com.littlecorgi.my.logic.model.Student;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 注册页面
 *
 * @author littlecorgi 2021/05/03
 */
public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding mBinding;
    private Student.DataBean mSignUpInfo;

    private Dialog mAvatarDialog;
    private Dialog mEmailDialog;
    private Dialog mPasswordDialog;
    private Dialog mNameDialog;
    private Dialog mPhoneDialog;
    private Dialog mPictureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mSignUpInfo = new Student.DataBean();
        // 因为initView需要用到initData的数据，所以把initData放前面
        initData();
        initView();
        initClick();
    }

    private void initView() {
        initBarColor();
        initAvatarView();
        initEmailView();
        initPasswordView();
        initNameView();
        initPhoneView();
        initPictureView();
        initClick();
    }

    private void initBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
        setSupportActionBar(mBinding.toolbar);
    }

    private void initAvatarView() {
        View avatarBtw = View.inflate(this, R.layout.my_picture_btw, null);
        final AppCompatTextView photo = avatarBtw.findViewById(R.id.picture_btw_photo);
        final AppCompatTextView album = avatarBtw.findViewById(R.id.picture_btw_album);
        final AppCompatTextView original = avatarBtw.findViewById(R.id.picture_btw_original);
        final AppCompatTextView cancel = avatarBtw.findViewById(R.id.picture_btw_cancel);
        photo.setOnClickListener(v1 -> photoHelp());
        album.setOnClickListener(v1 -> albumHelp());
        original.setOnClickListener(v1 -> originalHelp());
        cancel.setOnClickListener(v1 -> mAvatarDialog.dismiss());

        mBinding.signUpAvatar.setOnClickListener(v -> {
            if (mAvatarDialog != null) {
                mAvatarDialog.show();
            } else {
                mAvatarDialog = dialogBtw(avatarBtw, this);
            }
        });
    }

    private void initEmailView() {
        View emailBtw = View.inflate(this, R.layout.my_phone_btw, null);
        AppCompatEditText editText = emailBtw.findViewById(R.id.phone_edit_text);
        final boolean[] isEmailOk = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    editText.setError("邮箱格式不合法");
                    isEmailOk[0] = false;
                    return;
                }
                isEmailOk[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.signUpEmail.setOnClickListener(v -> {
            if (mEmailDialog != null) {
                mEmailDialog.show();
            } else {
                mEmailDialog = dialogBtw(emailBtw, this);
            }
        });
        AppCompatButton cancel = emailBtw.findViewById(R.id.phone_blw_cancelButton);
        AppCompatButton sure = emailBtw.findViewById(R.id.phone_blw_sureButton);
        cancel.setOnClickListener(v -> mEmailDialog.dismiss());
        sure.setOnClickListener(v -> {
            if (isEmailOk[0]) {
                String newEmail = Objects.requireNonNull(editText.getText()).toString();
                mEmailDialog.dismiss();
                mSignUpInfo.setEmail(newEmail);
                mBinding.signUpTvEmailDes.setText(newEmail);
            } else {
                showErrorToast(SignUpActivity.this, "账号不合法", true, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initPasswordView() {
        View passwordBtw = View.inflate(this, R.layout.my_phone_btw, null);
        AppCompatEditText editText = passwordBtw.findViewById(R.id.phone_edit_text);
        final boolean[] isPasswordOk = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 8) {
                    editText.setError("邮箱格式不合法");
                    isPasswordOk[0] = false;
                    return;
                }
                isPasswordOk[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.signUpPassword.setOnClickListener(v -> {
            if (mPasswordDialog != null) {
                mPasswordDialog.show();
            } else {
                mPasswordDialog = dialogBtw(passwordBtw, this);
            }
        });
        AppCompatButton cancel = passwordBtw.findViewById(R.id.phone_blw_cancelButton);
        AppCompatButton sure = passwordBtw.findViewById(R.id.phone_blw_sureButton);
        cancel.setOnClickListener(v -> mPasswordDialog.dismiss());
        sure.setOnClickListener(v -> {
            if (isPasswordOk[0]) {
                String newPassword = Objects.requireNonNull(editText.getText()).toString();
                mPasswordDialog.dismiss();
                mSignUpInfo.setPassword(newPassword);
                mBinding.signUpTvPasswordDes.setText(newPassword);
            } else {
                showErrorToast(SignUpActivity.this, "密码不合法", true, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initNameView() {
        View nameBtw = View.inflate(this, R.layout.my_phone_btw, null);
        AppCompatEditText editText = nameBtw.findViewById(R.id.phone_edit_text);
        final boolean[] isNameOk = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    editText.setError("请输入姓名");
                    isNameOk[0] = false;
                    return;
                }
                isNameOk[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.signUpName.setOnClickListener(v -> {
            if (mNameDialog != null) {
                mNameDialog.show();
            } else {
                mNameDialog = dialogBtw(nameBtw, this);
            }
        });
        AppCompatButton cancel = nameBtw.findViewById(R.id.phone_blw_cancelButton);
        AppCompatButton sure = nameBtw.findViewById(R.id.phone_blw_sureButton);
        cancel.setOnClickListener(v -> mNameDialog.dismiss());
        sure.setOnClickListener(v -> {
            if (isNameOk[0]) {
                String newName = Objects.requireNonNull(editText.getText()).toString();
                mNameDialog.dismiss();
                mSignUpInfo.setName(newName);
                mBinding.signUpTvNameDes.setText(newName);
            } else {
                showErrorToast(SignUpActivity.this, "姓名不合法", true, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initPhoneView() {
        View phoneBtw = View.inflate(this, R.layout.my_phone_btw, null);
        AppCompatEditText editText = phoneBtw.findViewById(R.id.phone_edit_text);
        final boolean[] isPhoneOk = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 11) {
                    editText.setError("长度必须是11位");
                    isPhoneOk[0] = false;
                    return;
                }
                Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                if (!pattern.matcher(s).matches()) {
                    editText.setError("手机号不能有数字之外的元素");
                    isPhoneOk[0] = false;
                    return;
                }
                isPhoneOk[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.signUpPhone.setOnClickListener(v -> {
            if (mPhoneDialog != null) {
                mPhoneDialog.show();
            } else {
                mPhoneDialog = dialogBtw(phoneBtw, this);
            }
        });
        AppCompatButton cancel = phoneBtw.findViewById(R.id.phone_blw_cancelButton);
        AppCompatButton sure = phoneBtw.findViewById(R.id.phone_blw_sureButton);
        cancel.setOnClickListener(v -> mPhoneDialog.dismiss());
        sure.setOnClickListener(v -> {
            if (isPhoneOk[0]) {
                String newPhone = Objects.requireNonNull(editText.getText()).toString();
                mPhoneDialog.dismiss();
                mSignUpInfo.setPhone(newPhone);
                mBinding.signUpTvPhoneDes.setText(newPhone);
            } else {
                showErrorToast(SignUpActivity.this, "手机号不合法", true, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initPictureView() {
        View pictureBtw = View.inflate(this, R.layout.my_picture_btw, null);
        final AppCompatTextView photo = pictureBtw.findViewById(R.id.picture_btw_photo);
        final AppCompatTextView album = pictureBtw.findViewById(R.id.picture_btw_album);
        final AppCompatTextView original = pictureBtw.findViewById(R.id.picture_btw_original);
        final AppCompatTextView cancel = pictureBtw.findViewById(R.id.picture_btw_cancel);
        photo.setOnClickListener(v1 -> photoHelp());
        album.setOnClickListener(v1 -> albumHelp());
        original.setOnClickListener(v1 -> originalHelp());
        cancel.setOnClickListener(v1 -> mPictureDialog.dismiss());

        mBinding.signUpPicture.setOnClickListener(v -> {
            if (mPictureDialog != null) {
                mPictureDialog.show();
            } else {
                mPictureDialog = dialogBtw(pictureBtw, this);
            }
        });
    }

    private void photoHelp() {
        /*
        拍照
         */
        openCamera(this, OPEN_CAMERA);
        mAvatarDialog.dismiss();
    }

    private void albumHelp() {
        /*
        打开相册
         */
        openAlbum(this, OPEN_ALBUM);
        mAvatarDialog.dismiss();
    }

    private void originalHelp() {
        /*
        查看大图
         */
        startOriginalActivity(this, mSignUpInfo.getAvatar());
        mAvatarDialog.dismiss();
    }

    private void initData() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        mBinding.signUpTvEmailDes.setText(email);
        mBinding.signUpTvPasswordDes.setText(password);
        mSignUpInfo.setEmail(email);
        mSignUpInfo.setPassword(password);
    }

    private void initClick() {
        mBinding.toolbar.setNavigationOnClickListener(v -> finish());
        mBinding.signUpBtnSure.setOnClickListener(v -> signUp());
    }

    /**
     * 进行网络请求
     */
    private void signUp() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_CAMERA || requestCode == OPEN_ALBUM) {
                String path = getImagePath(data);
                Glide.with(this).load(path).into(mBinding.signUpIvAvatar);
                mSignUpInfo.setAvatar(path);
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

    /**
     * 跳转到该Activity
     *
     * @param context  上下文
     * @param email    邮箱
     * @param password 密码
     */
    public static void startSignUpActivity(Context context, String email, String password) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        context.startActivity(intent);
    }
}
package com.littlecorgi.leave.ui;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.ui.adapter.SelectPlotAdapter;
import com.littlecorgi.leave.ui.util.GlideEngine;
import com.littlecorgi.leave.ui.util.Tools;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生请假页面Fragment
 */
public class AskLeaveFragment extends Fragment {

    private static final String TAG = "AskLeave";

    private RecyclerView mRecycler;
    private SelectPlotAdapter mAdapter;
    private ArrayList<String> mAllSelectList; // 所有图片集合
    private ArrayList<String> mCategoryLists; // 查看图片集合
    private final List<LocalMedia> mSelectList = new ArrayList<>();

    private EditText mNameEditText;
    private EditText mClassTextEditText;

    private RadioGroup mRadioGroupType1;
    private RadioButton mSickButton;
    private RadioButton mThingButton;
    private RadioButton mOtherButton;
    private RadioGroup mRadioGroupType2;
    private RadioButton mYesButton;
    private RadioButton mNoButton;

    private EditText mStartTimeEditText;
    private EditText mEndTimeEditText;
    private EditText mPlaceEditText;
    private EditText mMyPhoneEditText;
    private EditText mOtherPhoneEditText;
    private EditText mLeaveSituationEditText;

    private ImageView mAddPictureImage;
    private Button mSubmitButton;

    private String mType1;
    private RadioButton mRadioButton1;
    private String mSelectText1;
    private String mType2;
    private RadioButton mRadioButton2;
    private String mSelectText2;

    private String mNameText;
    private String mClassText;
    private String mStartTimeText;
    private String mEndTimeText;
    private String mPlaceText;
    private String mMyPhoneText;
    private String mOtherPhoneText;
    private String mLeaveSituationText;

    private Handler mHandler;
    private Animator mAnimator;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ask_leave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameEditText = requireActivity().findViewById(R.id.edit_text_name);
        mClassTextEditText = requireActivity().findViewById(R.id.edit_text_class);

        mRadioGroupType1 = requireActivity().findViewById(R.id.rg_type1);
        mSickButton = requireActivity().findViewById(R.id.rg_sick);
        mThingButton = requireActivity().findViewById(R.id.rg_thing);
        mOtherButton = requireActivity().findViewById(R.id.rg_other);
        mRadioGroupType2 = requireActivity().findViewById(R.id.rg_type2);
        mYesButton = requireActivity().findViewById(R.id.rg_yes);
        mNoButton = requireActivity().findViewById(R.id.rg_no);

        mStartTimeEditText = requireActivity().findViewById(R.id.edit_text_start_time);
        mEndTimeEditText = requireActivity().findViewById(R.id.edit_text_end_time);
        mPlaceEditText = requireActivity().findViewById(R.id.edit_text_place);
        mMyPhoneEditText = requireActivity().findViewById(R.id.edit_text_my_phone);
        mOtherPhoneEditText = requireActivity().findViewById(R.id.edit_text_other_phone);
        mLeaveSituationEditText =
                requireActivity().findViewById(R.id.edit_text_leave_situation);

        mSubmitButton = requireActivity().findViewById(R.id.submit);

        mRecycler = requireActivity().findViewById(R.id.recycler);

        mRadioGroupType1.setOnCheckedChangeListener((group, checkedId) -> {
            mRadioButton1 = requireActivity()
                    .findViewById(mRadioGroupType1.getCheckedRadioButtonId());
            mSelectText1 = mRadioButton1.getText().toString();
        });
        mRadioGroupType2.setOnCheckedChangeListener((group, checkedId) -> {
            mRadioButton2 = requireActivity()
                    .findViewById(mRadioGroupType2.getCheckedRadioButtonId());
            mSelectText2 = mRadioButton2.getText().toString();
        });

        // addPictureImage.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         showDialog();
        //     }
        // });

        mSubmitButton.setOnClickListener((OnClickListener) v -> {
            mNameText = mNameEditText.getText().toString();
            mClassText = mClassTextEditText.getText().toString();
            mStartTimeText = mStartTimeEditText.getText().toString();
            mEndTimeText = mEndTimeEditText.getText().toString();
            mPlaceText = mPlaceEditText.getText().toString();
            mMyPhoneText = mMyPhoneEditText.getText().toString();
            mOtherPhoneText = mOtherPhoneEditText.getText().toString();
            mLeaveSituationText = mLeaveSituationEditText.getText().toString();
            Editor editor = requireActivity()
                    .getSharedPreferences("data", Context.MODE_PRIVATE)
                    .edit();
            editor.putString("name", mNameText);
            editor.putString("class", mClassText);
            editor.putString("type1", mSelectText1);
            editor.putString("type2", mSelectText2);
            editor.putString("startTime", mStartTimeText);
            editor.putString("endTime", mEndTimeText);
            editor.putString("place", mPlaceText);
            editor.putString("myPhone", mMyPhoneText);
            editor.putString("otherPhone", mOtherPhoneText);
            editor.putString("leaveSituation", mLeaveSituationText);
            editor.apply();

            // TabLayout tabLayout = getActivity().findViewById(R.id.view_up_line);
            // tabLayout.getTabAt(1).select();
            // HistoryFragment histroyFragment = new HistoryFragment();
            // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            // FragmentTransaction transaction = fragmentManager.beginTransaction();
            // transaction.replace(R.id.ask_leave,histroyFragment);
            // transaction.commit();
            ViewPager viewPager = requireActivity().findViewById(R.id.leave_viewpager);
            // viewPager.getAdapter().notifyDataSetChanged();
            viewPager.setCurrentItem(1);
        });

        // 添加多张图片
        if (null == mAllSelectList) {
            mAllSelectList = new ArrayList<>();
        }
        if (null == mCategoryLists) {
            mCategoryLists = new ArrayList<>();
        }
        Tools.requestPermissions((AppCompatActivity) getActivity());
        initAdapter();
    }

    private void initAdapter() {
        // 最多九张有图片
        mAdapter = new SelectPlotAdapter(requireActivity().getApplicationContext(), 9);
        mRecycler
                .setLayoutManager(
                        new GridLayoutManager(requireActivity().getApplicationContext(), 3));
        mAdapter.setImageList(mAllSelectList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setListener(new SelectPlotAdapter.CallbackListener() {
            @Override
            public void add() {
                int size = 9 - mAllSelectList.size();
                Tools.galleryPictures((AppCompatActivity) getActivity(), size);
            }

            @Override
            public void delete(int position) {
                mAllSelectList.remove(position);
                mCategoryLists.remove(position);
                mAdapter.setImageList(mAllSelectList); // 再set所有集合
            }

            @SuppressWarnings("checkstyle:CommentsIndentation")
            @Override
            public void item(int position, List<String> list) {
                mSelectList.clear();
                for (int i = 0; i < mAllSelectList.size(); i++) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(mAllSelectList.get(i));
                    mSelectList.add(localMedia);
                }
                // ①、图片选择器自带预览
                PictureSelector.create(getActivity())
                        .themeStyle(R.style.picture_default_style)
                        .isNotPreviewDownload(true) // 是否显示保存弹框
                        .imageEngine(GlideEngine.createGlideEngine()) // 选择器展示不出图片则添加
                        .openExternalPreview(position, mSelectList);

                        /* 此处是CheckStyle有问题，所以/*才会这么后面
                // ②:自定义布局预览
                // Tools.startPhotoViewActivity(MainActivity.this, categoryLists, position);
                        */
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // 结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            showSelectPic(selectList);
        }
    }

    private void showSelectPic(List<LocalMedia> result) {
        for (int i = 0; i < result.size(); i++) {
            String path;
            // 判断是否10.0以上
            if (Build.VERSION.SDK_INT >= 29) {
                path = result.get(i).getAndroidQToPath();
            } else {
                path = result.get(i).getPath();
            }
            mAllSelectList.add(path);
            mCategoryLists.add(path);
            Log.e(TAG, "图片链接: " + path);
        }
        mAdapter.setImageList(mAllSelectList);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        View view = View.inflate(getContext(), R.layout.layout_dialog_custom, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.main_menu_animStyle);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.tv_take_photo)
                .setOnClickListener(v -> takePhoto());
        dialog.findViewById(R.id.tv_choose_picture)
                .setOnClickListener(v -> choosePicture());
        dialog.findViewById(R.id.tv_cancel)
                .setOnClickListener(v -> dialog.dismiss());
    }

    private void takePhoto() {
    }

    private void choosePicture() {
    }
}

package com.littlecorgi.leave.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.littlecorgi.commonlib.AppViewModel;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.databinding.LayoutAskLeaveBinding;
import com.littlecorgi.leave.logic.ClassRetrofitRepository;
import com.littlecorgi.leave.logic.LeaveRepository;
import com.littlecorgi.leave.logic.model.AllClassResponse;
import com.littlecorgi.leave.logic.model.CreateLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveRequest;
import com.littlecorgi.leave.ui.adapter.SelectPlotAdapter;
import com.littlecorgi.leave.ui.util.GlideEngine;
import com.littlecorgi.leave.ui.util.Tools;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 学生请假页面Fragment
 */
public class AskLeaveFragment extends Fragment {

    private static final String TAG = "AskLeaveFragment";

    private LayoutAskLeaveBinding mBinding;
    private AppViewModel mViewModel;

    private RecyclerView mRecycler;
    private SelectPlotAdapter mAdapter;
    private ArrayList<String> mAllSelectList; // 所有图片集合
    private ArrayList<String> mCategoryLists; // 查看图片集合
    private final List<LocalMedia> mSelectList = new ArrayList<>();

    private ArrayAdapter<String> mSpinnerAdapter;
    private final ArrayList<String> mClassList = new ArrayList<>();
    private final ArrayList<AllClassResponse.DataBean> mAllClass = new ArrayList<>();

    private ImageView mAddPictureImage;
    private Button mSubmitButton;

    private RadioButton mRadioButton1;
    private String mSelectText1;
    private RadioButton mRadioButton2;
    private String mSelectText2;

    private long mClassId;
    private long mStudentId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_ask_leave, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        Log.d(TAG, "onViewCreated: " + mViewModel);
        mStudentId = mViewModel.getStudentId();

        initView();
        initEvent();

        // 添加多张图片
        if (null == mAllSelectList) {
            mAllSelectList = new ArrayList<>();
        }
        if (null == mCategoryLists) {
            mCategoryLists = new ArrayList<>();
        }
        Tools.requestPermissions((AppCompatActivity) getActivity());
        initAdapter();
        initSpinner();

        if (mStudentId != -1) {
            initData();
        } else {
            Toast.makeText(requireContext(), "获取不到用户信息，请登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mSubmitButton = requireActivity().findViewById(R.id.submit);
        mRecycler = requireActivity().findViewById(R.id.recycler);

        mBinding.editTextStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                try {
                    dateFormat.parse(s.toString());
                } catch (ParseException e) {
                    mBinding.editTextStartTime.setError("日期格式必须是yyyy-MM-dd HH:mm:ss");
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBinding.editTextEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                try {
                    dateFormat.parse(s.toString());
                } catch (ParseException e) {
                    mBinding.editTextStartTime.setError("日期格式必须是yyyy-MM-dd HH:mm:ss");
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initEvent() {
        mBinding.layoutAskLeaveRefresh.setEnableRefresh(true);
        mBinding.layoutAskLeaveRefresh.setOnRefreshListener(v -> {
            mStudentId = mViewModel.getStudentId();
            if (mStudentId != -1) {
                initData();
            }
            v.finishRefresh(true);
        });
        mBinding.rgType1.setOnCheckedChangeListener((group, checkedId) -> {
            mRadioButton1 = requireActivity()
                    .findViewById(mBinding.rgType1.getCheckedRadioButtonId());
            mSelectText1 = mRadioButton1.getText().toString();
        });
        mBinding.rgType2.setOnCheckedChangeListener((group, checkedId) -> {
            mRadioButton2 = requireActivity()
                    .findViewById(mBinding.rgType2.getCheckedRadioButtonId());
            mSelectText2 = mRadioButton2.getText().toString();
        });
        mSubmitButton.setOnClickListener(v -> {
            String startTimeString = mBinding.editTextStartTime.getText().toString();
            String endTimeString = mBinding.editTextEndTime.getText().toString();
            try {
                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                dateFormat.parse(startTimeString);
                dateFormat.parse(endTimeString);
            } catch (ParseException e) {
                Toast.makeText(requireContext(), "日期格式必须是yyyy-MM-dd HH:mm:ss", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (mSelectText1.isEmpty()
                    || mBinding.editTextLeaveSituation.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "数据未填写，请填写后再提交", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setStates(0);
            leaveRequest.setTitle(mSelectText1);
            leaveRequest.setDescription(mBinding.editTextLeaveSituation.getText().toString());
            long startTime = TimeUtil.INSTANCE.getTimestampFromTime(startTimeString);
            leaveRequest.setStartTime(startTime);
            long endTime = TimeUtil.INSTANCE.getTimestampFromTime(endTimeString);
            leaveRequest.setEndTime(endTime);

            createLeave(leaveRequest);
        });
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

    private void initSpinner() {
        mSpinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, mClassList);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sniperClass.setAdapter(mSpinnerAdapter);

        mBinding.sniperClass.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                        mClassId = mAllClass.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    private void initData() {
        getAllClass();
    }

    private void getAllClass() {
        Dialog loading = DialogUtil.writeLoadingDialog(requireContext(), false, "获取班级中");
        loading.show();
        loading.setCancelable(false);
        ClassRetrofitRepository.getAllClassFromTheStudentCall(mStudentId).enqueue(
                new Callback<AllClassResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AllClassResponse> call,
                                           @NonNull Response<AllClassResponse> response) {
                        loading.cancel();
                        Log.d(TAG, "onResponse: " + response.toString());
                        assert response.body() != null;
                        if (response.body().getStatus() == 800) {
                            List<AllClassResponse.DataBean> classList = response.body().getData();
                            mAllClass.clear();
                            mAllClass.addAll(classList);
                            convertClassBeanToSpinnerString();
                        } else {
                            Log.d(TAG, "onResponse: 发生错误");
                            Toast.makeText(requireContext(),
                                    "发生错误，错误信息：" + response.body().getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AllClassResponse> call,
                                          @NonNull Throwable t) {
                        loading.cancel();
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        t.printStackTrace();
                        Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createLeave(LeaveRequest leaveRequest) {
        Dialog loading = DialogUtil.writeLoadingDialog(requireContext(), false, "获取班级中");
        loading.show();
        loading.setCancelable(false);
        LeaveRepository.createLeave(mStudentId, mClassId, leaveRequest).enqueue(
                new Callback<CreateLeaveResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateLeaveResponse> call,
                                           @NonNull Response<CreateLeaveResponse> response) {
                        loading.cancel();
                        Log.d(TAG, "onResponse: " + response.toString());
                        assert response.body() != null;
                        if (response.body().getStatus() == 800) {
                            Toast.makeText(requireContext(), "创建成功：" + response.body().getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            // 跳转到请假记录页面
                            ViewPager viewPager =
                                    requireActivity().findViewById(R.id.leave_viewpager);
                            viewPager.setCurrentItem(1);
                        } else {
                            Log.d(TAG, "onResponse: 发生错误");
                            Toast.makeText(requireContext(),
                                    "发生错误，错误信息：" + response.body().getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateLeaveResponse> call,
                                          @NonNull Throwable t) {
                        loading.cancel();
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        t.printStackTrace();
                        Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void convertClassBeanToSpinnerString() {
        mClassList.clear();
        for (AllClassResponse.DataBean dataBean : mAllClass) {
            mClassList.add(dataBean.getName() + "-" + dataBean.getTeacher().getName());
        }
        mSpinnerAdapter.notifyDataSetChanged();
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

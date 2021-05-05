package com.littlecorgi.leave.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.leave.R;
import com.littlecorgi.leave.databinding.LeaveSituationBinding;
import com.littlecorgi.leave.logic.LeaveRepository;
import com.littlecorgi.leave.logic.model.GetLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveBean;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请假记录详情
 */
public class PeopleHistoryFragment extends Fragment {

    private static final String TAG = "PeopleHistoryFragment";

    private final long mLeaveId;

    private Button mButtonReturn;
    private Button mTextViewReturn;

    public LocationClient mLocationClient;

    private LeaveSituationBinding mBinding;

    public PeopleHistoryFragment(long leaveId) {
        mLeaveId = leaveId;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.leave_situation, container, false);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permission = permissionList.toArray(new String[0]);
            ActivityCompat.requestPermissions(requireActivity(), permission, 1);
        } else {
            requestLocation();
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        getData();
        setEvent();
    }

    private void findView(View view) {
        mButtonReturn = view.findViewById(R.id.btn_return);
        mTextViewReturn = view.findViewById(R.id.tv_return);
        mBinding.xiaojia.setVisibility(View.GONE);
    }

    private void getData() {
        Dialog loading = DialogUtil.writeLoadingDialog(requireContext(), false, "加载中");
        loading.show();
        loading.setCancelable(false);
        LeaveRepository.getLeaveInfo(mLeaveId).enqueue(new Callback<GetLeaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetLeaveResponse> call,
                                   @NonNull Response<GetLeaveResponse> response) {
                loading.cancel();
                Log.d(TAG, "onResponse: " + response.toString());
                GetLeaveResponse getLeaveResponse = response.body();
                assert getLeaveResponse != null;
                showData(getLeaveResponse.getData());
            }

            @Override
            public void onFailure(@NonNull Call<GetLeaveResponse> call, @NonNull Throwable t) {
                loading.cancel();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData(LeaveBean leave) {
        mBinding.nameText.setText(leave.getStudent().getName());
        mBinding.type1Text.setText(leave.getTitle());
        mBinding.type2Text.setText("否");
        mBinding.startNameText
                .setText(TimeUtil.INSTANCE.getTimeFromTimestamp(leave.getStartTime()));
        mBinding.endTimeText.setText(TimeUtil.INSTANCE.getTimeFromTimestamp(leave.getEndTime()));
        mBinding.placeText.setText("长安校区");
        mBinding.myPhoneText.setText(leave.getStudent().getPhone());
        mBinding.otherPhoneText.setText(leave.getStudent().getPhone());
        mBinding.reasonText.setText(leave.getDescription());
        Glide.with(requireContext()).load(leave.getStudent().getAvatar())
                .into(mBinding.ivStudentAvatar);


        mBinding.teacherName.setText(leave.getClassDetail().getTeacher().getName());
        mBinding.teacherClass.setText(leave.getClassDetail().getName());
        String agreeState;
        if (leave.getStates() == 1) {
            agreeState = "不批准";
        } else if (leave.getStates() == 2) {
            agreeState = "批准";
        } else {
            agreeState = "待审核";
        }
        mBinding.isAgree.setText(agreeState);
        Glide.with(requireContext()).load(leave.getClassDetail().getTeacher().getAvatar())
                .into(mBinding.ivTeacherAvatar);
    }

    private void setEvent() {
        mButtonReturn.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
        mTextViewReturn.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                requestLocation();
            } else {
                Toast.makeText(requireActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 定位接口
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(final BDLocation location) {
            requireActivity().runOnUiThread(() -> {
                String currentPosition = location.getCountry() + " "
                        + location.getProvince() + " "
                        + location.getCity() + " "
                        + location.getDistrict() + "\n";
                Log.d(TAG, currentPosition);
                mBinding.locationText.setText("定位的地点  " + currentPosition);
            });
        }
    }
}

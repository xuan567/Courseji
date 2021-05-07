package com.littlecorgi.leave;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.leave.databinding.ActivityPeopleHistoryBinding;
import com.littlecorgi.leave.logic.LeaveRepository;
import com.littlecorgi.leave.logic.model.GetLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveBean;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 历史记录具体详情类
 *
 * @author littlecorgi 2021/05/07
 */
public class PeopleHistoryActivity extends AppCompatActivity {

    private static final String TAG = "PeopleHistoryActivity";

    private long mLeaveId;

    public LocationClient mLocationClient;

    private ActivityPeopleHistoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_people_history);

        mBinding.toolbar.setNavigationOnClickListener(v -> finish());

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        mLeaveId = getIntent().getLongExtra("mLeaveId", -1L);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permission = permissionList.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permission, 1);
        } else {
            requestLocation();
        }

        findView();
        if (mLeaveId != -1) {
            getData();
        }
    }

    private void findView() {
        mBinding.xiaojia.setVisibility(View.GONE);
    }

    private void getData() {
        Dialog loading = DialogUtil.writeLoadingDialog(this, false, "加载中");
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
                Toast.makeText(PeopleHistoryActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
        Glide.with(this).load(leave.getStudent().getAvatar())
                .into(mBinding.ivStudentAvatar);


        mBinding.teacherName.setText(leave.getClassDetail().getTeacher().getName());
        mBinding.teacherClass.setText(leave.getClassDetail().getName());
        String agreeState;
        if (leave.getStates() == 1) {
            agreeState = "批准";
        } else if (leave.getStates() == 2) {
            agreeState = "不批准";
        } else {
            agreeState = "待审核";
        }
        mBinding.isAgree.setText(agreeState);
        Glide.with(this).load(leave.getClassDetail().getTeacher().getAvatar())
                .into(mBinding.ivTeacherAvatar);
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
                        Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                requestLocation();
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 定位接口
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(() -> {
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
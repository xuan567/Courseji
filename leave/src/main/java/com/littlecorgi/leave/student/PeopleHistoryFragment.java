package com.littlecorgi.leave.student;

import static android.content.Context.VIBRATOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.littlecorgi.leave.R;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PeopleHistoryFragment extends Fragment {

    private Button mButtonReturn;
    private Button mTextViewReturn;

    private TextView mNameText;
    private TextView mStartTimeText;
    private TextView mEndTimeText;
    private TextView mType1Text;
    private TextView mType2Text;
    private TextView mPlaceText;
    private TextView mMyPhoneText;
    private TextView mOtherPhoneText;
    private TextView mReasonText;

    private String mName;
    private String mNumber;
    private String mStartTime;
    private String mEndTime;
    private String mType1;
    private String mType2;
    private String mPlace;
    private String mMyPhone;
    private String mOtherPhone;
    private String mReason;
    private ImageView mLocationImage;

    // 销假按钮
    private Button mResumptionButton;

    private TextView mPositionText;
    public LocationClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        View view = inflater.inflate(R.layout.leave_situation, container, false);
        mPositionText = view.findViewById(R.id.location_text);
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
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        getData();
        showData();
        setEvent();
    }

    private void findView(View view) {
        mButtonReturn = view.findViewById(R.id.btn_return);
        mTextViewReturn = view.findViewById(R.id.tv_return);
        mNameText = view.findViewById(R.id.name_text);
        mStartTimeText = view.findViewById(R.id.start_name_text);
        mEndTimeText = view.findViewById(R.id.end_time_text);
        mType1Text = view.findViewById(R.id.type1_text);
        mType2Text = view.findViewById(R.id.type2_text);
        mPlaceText = view.findViewById(R.id.place_text);
        mMyPhoneText = view.findViewById(R.id.my_phone_text);
        mOtherPhoneText = view.findViewById(R.id.other_phone_text);
        mReasonText = view.findViewById(R.id.reason_text);
        mResumptionButton = view.findViewById(R.id.xiaojia);
        mLocationImage = view.findViewById(R.id.location_image);
        mLocationImage.setVisibility(View.GONE);
    }

    private void getData() {
        SharedPreferences pref =
                requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mName = pref.getString("name", "");
        mType1 = pref.getString("type1", "");
        mType2 = pref.getString("type2", "");
        mStartTime = pref.getString("startTime", "");
        mEndTime = pref.getString("endTime", "");
        mPlace = pref.getString("place", "");
        mMyPhone = pref.getString("myPhone", "");
        mOtherPhone = pref.getString("otherPhone", "");
        mReason = pref.getString("leaveSituation", "");
    }

    private void showData() {
        mNameText.setText(mName);
        mType1Text.setText(mType1);
        mType2Text.setText(mType2);
        mStartTimeText.setText(mStartTime);
        mEndTimeText.setText(mEndTime);
        mPlaceText.setText(mPlace);
        mMyPhoneText.setText(mMyPhone);
        mOtherPhoneText.setText(mOtherPhone);
        mReasonText.setText(mReason);
    }

    private void setEvent() {
        mResumptionButton.setOnClickListener(v -> {
                    mResumptionButton.setText("已销假");
                    mResumptionButton.setBackgroundResource(R.drawable.button_shape2);
                    Vibrator vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    mLocationImage.setVisibility(View.VISIBLE);
                    requestLocation();
                    Toast.makeText(requireActivity(), "销假成功", Toast.LENGTH_SHORT).show();
                }
        );
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
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            requireActivity().runOnUiThread(() -> {
                String currentPosition = location.getCountry() + " "
                        + location.getProvince() + " "
                        + location.getCity() + " "
                        + location.getDistrict() + "\n";
                Log.d("location", currentPosition);
                mPositionText.setText("定位的地点");
            });
        }
    }
}

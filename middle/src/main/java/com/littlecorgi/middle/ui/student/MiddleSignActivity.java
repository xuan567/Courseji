package com.littlecorgi.middle.ui.student;

import static com.littlecorgi.middle.logic.dao.Tool.FaceRecognition;
import static com.littlecorgi.middle.logic.dao.Tool.SBlueTooth;
import static com.littlecorgi.middle.logic.dao.Tool.SFaceLocation;
import static com.littlecorgi.middle.logic.dao.Tool.SFinish;
import static com.littlecorgi.middle.logic.dao.Tool.SLeave;
import static com.littlecorgi.middle.logic.dao.Tool.SLocation;
import static com.littlecorgi.middle.logic.dao.Tool.SNormal;
import static com.littlecorgi.middle.logic.dao.Tool.SOG;
import static com.littlecorgi.middle.logic.dao.Tool.STookPhoto;
import static com.littlecorgi.middle.logic.dao.Tool.SUnFinish;
import static com.littlecorgi.middle.logic.dao.WindowHelp.setWindowStatusBarColor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.google.gson.Gson;
import com.littlecorgi.commonlib.BaseActivity;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.middle.R;
import com.littlecorgi.middle.logic.CheckOnRepository;
import com.littlecorgi.middle.logic.RetrofitRepository;
import com.littlecorgi.middle.logic.dao.BaiDuMapService;
import com.littlecorgi.middle.logic.dao.LocationService;
import com.littlecorgi.middle.logic.dao.PassedIngLat;
import com.littlecorgi.middle.logic.model.CheckInResponse;
import com.littlecorgi.middle.logic.model.FaceRecognitionResponse;
import com.littlecorgi.middle.logic.model.LogAndLat;
import com.littlecorgi.middle.logic.model.Sign;
import com.littlecorgi.middle.logic.network.MyLocationListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 签到Activity 这里本来是要根据签到方式的不同展示不同的签页面，把签到方式的页面分为四种，目前使用的是用地图做展示 未完成的： 人脸识别完成签到
 */
public class MiddleSignActivity extends BaseActivity {

    private static final String TAG = "MiddleSignActivity";

    private AppCompatTextView mReturnButton;
    private BaiDuMapService mBaiDuMapService;
    private LocationService mLocationService;
    private MapView mMapView;
    private Sign mSign;
    private Uri mPicUri;
    private int mLabel;
    private double mLat;
    private double mLng;
    private int mRadius;
    private boolean mIsHaveMapView = false;
    private boolean mIsIn = false;
    private View mLastView; // 被删除前的view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.middle_sign);

        Intent intent = getIntent();
        mSign = (Sign) intent.getSerializableExtra("sign");
        mPicUri = intent.getParcelableExtra("picUri");
        mRadius = mSign.getRadius();

        initView();
        initData();
    }

    private void initView() {
        changeBarColor();
        initFind();
        initClick();
    }

    private void changeBarColor() {
        setWindowStatusBarColor(this, R.color.blue);
    }

    private void initFind() {
        mReturnButton = findViewById(R.id.middle_sign_returnButton);
    }

    private void initClick() {
        mReturnButton.setOnClickListener(v -> finish());
    }

    private void initData() {
        assert mSign != null;
        int state = mSign.getState();
        mLabel = mSign.getLabel();

        // 根据签到状态的不同显示不同的页面
        switch (state) {
            case SOG: // 进行中
                initOnGoing();
                break;
            case SUnFinish: // 未完成
                initUnFinish();
                break;
            case SLeave: // 请假的
                initSLeave();
                break;
            case SFinish: // 已完成
                initSFinish();
                break;
            default:
                Log.e(TAG, "initData: switch分支错误，state没有定义");
                break;
        }
    }

    private View addView(int layout) {
        View view = View.inflate(this, layout, null);
        ConstraintLayout.LayoutParams lp =
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT);
        this.addContentView(view, lp);
        return view;
    }

    private void initOnGoing() {
        // 目前仅考虑定位+人脸识别
        switch (mLabel) {
            case SNormal:
                onGoingNormal();
                break;
            case FaceRecognition:
            case SBlueTooth:
            case STookPhoto:
            case SLocation:
                break;
            case SFaceLocation:
                onGoingFaceLocation();
                break;
            default:
                Log.e(TAG, "initOnGoing: switch分支错误，state没有定义");
                break;
        }
    }

    private void onGoingNormal() {
    }

    private void onGoingFaceLocation() {
        doFaceRecognition();
        doLocation();
    }

    private void doFaceRecognition() {
        // 显示人脸识别Dialog
        Dialog progressDialog = DialogUtil.writeLoadingDialog(this, false, "人脸识别中");
        progressDialog.show();
        //设置点击屏幕加载框不会取消（返回键可以取消）
        progressDialog.setCanceledOnTouchOutside(true);

        File file = new File(mPicUri.getPath());
        RetrofitRepository.getFaceRecognitionCall(1L, true, false, file)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        boolean isSuccess = false;
                        String errorMessage = "";
                        progressDialog.cancel();
                        if (response.body() != null) {
                            try {
                                String json = response.body().string();
                                FaceRecognitionResponse faceRecognitionResponse = new Gson()
                                        .fromJson(json, FaceRecognitionResponse.class);
                                Log.d(TAG, "onResponse: " + faceRecognitionResponse);
                                if (faceRecognitionResponse.getStatus() == 800) {
                                    if (faceRecognitionResponse.getData() >= 40) {
                                        Toast.makeText(MiddleSignActivity.this, "人脸识别成功",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        isSuccess = true;
                                    } else {
                                        errorMessage = "人脸识别未通过";
                                    }
                                } else {
                                    errorMessage =
                                            "人脸识别错误，" + faceRecognitionResponse.getErrorMsg();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                errorMessage = "接口异常" + e.getMessage();
                            }
                        } else {
                            Log.d(TAG, "onResponse: response.body() is null!!!");
                        }
                        file.delete();
                        if (!isSuccess) {
                            showFaceRecognitionFailureDialog(errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        progressDialog.cancel();
                        t.printStackTrace();
                        file.delete();
                        showFaceRecognitionFailureDialog("网络错误");
                    }
                });
    }

    private void doLocation() {
        View view = addView(R.layout.middle_signongoing_face_location);
        mLastView = view;
        AppCompatTextView countdown = view.findViewById(R.id.OGFL_Countdown);
        mMapView = view.findViewById(R.id.OGFL_MapView);
        final AppCompatTextView text = view.findViewById(R.id.OGFL_mapText);
        final AppCompatButton sureButton = view.findViewById(R.id.OGFL_Button);

        // 倒计时：
        try {
            setCountdown(countdown);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 设置地图
        setMapView(text);

        sureButton.setOnClickListener(
                v -> {
                    if (mIsIn) {
                        response();
                    } else {
                        Toast.makeText(MiddleSignActivity.this, "请移步到指定范围内签到", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    /**
     * 设置倒计时
     *
     * @param view 倒计时的TextView
     */
    private void setCountdown(AppCompatTextView view) throws ParseException {
        long endTime = mSign.getEndTime();
        long nowTime = new Date().getTime();
        long millisUntilFinished = endTime - nowTime;
        // CountDownTimer 类实现倒计时功能
        CountDownTimer countDownTimer =
                new CountDownTimer(millisUntilFinished, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                        long hours =
                                (millisUntilFinished - days * (1000 * 60 * 60 * 24)) / (1000 * 60
                                        * 60);
                        long minutes =
                                (millisUntilFinished - days * (1000 * 60 * 60 * 24) - hours * (1000
                                        * 60 * 60))
                                        / (1000 * 60);
                        long second =
                                (millisUntilFinished
                                        - days * (1000 * 60 * 60 * 24)
                                        - hours * (1000 * 60 * 60)
                                        - minutes * 1000 * 60)
                                        / (1000);
                        String time = hours + ":" + minutes + ":" + second;
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert date != null;
                        String countdown = simpleDateFormat.format(date);
                        if (days != 0) {
                            view.setText(days + "天 " + countdown);
                        } else {
                            view.setText(countdown);
                        }
                    }

                    @Override
                    public void onFinish() {
                        ((ViewGroup) mLastView.getParent()).removeView(mLastView); // 删除上个视图
                        initUnFinish();
                    }
                };
        countDownTimer.start();
    }

    private void setMapView(AppCompatTextView text) {
        mIsHaveMapView = true;
        mBaiDuMapService = new BaiDuMapService(mMapView.getMap());
        mLocationService = new LocationService(this);

        MyLocationListener myListener =
                new MyLocationListener(
                        mBaiDuMapService,
                        new PassedIngLat() {
                            @Override
                            public void location(String lat, String lng, String cty) {
                                mLat = Double.parseDouble(lat);
                                mLng = Double.parseDouble(lng);
                                Log.d(TAG, "location: lat = " + lat + " ing = " + lng);
                                Log.d(TAG, "location: mSign lat = " + mSign.getLat()
                                        + " ing = " + mSign.getLng());
                                LatLng center = new LatLng(mSign.getLat(), mSign.getLng());
                                mBaiDuMapService.addMarker(center);
                                mBaiDuMapService.setCircle(center, mRadius);
                                LatLng point = new LatLng(mLat, mLng);
                                mIsIn = SpatialRelationUtil
                                        .isCircleContainsPoint(center, mRadius, point);
                                if (mIsIn) {
                                    text.setText("已在指定范围内");
                                    text.setTextColor(getResources().getColor(R.color.finish));
                                } else {
                                    text.setText("未在指定范围内，请走到指定范围");
                                    text.setTextColor(getResources().getColor(R.color.warning));
                                }
                            }

                            @Override
                            public void floor(String lat, String ing, String address) {
                                //获取室内定位
                            }

                            @Override
                            public void polLocation(List<Poi> list) {
                            }
                        });
        mLocationService.registerListener(myListener);
        mLocationService.start();
    }

    /**
     * 签到请求发起
     */
    private void response() {
        Dialog loading = DialogUtil.writeLoadingDialog(this, false, "签到中");
        loading.show();
        loading.setCancelable(false);
        LogAndLat logAndLat = new LogAndLat();
        logAndLat.setLatitude(mLat);
        logAndLat.setLongitude(mLng);
        Call<CheckInResponse> checkInResponseCall =
                CheckOnRepository.checkIn(mSign.getStudentId(), mSign.getAttendanceId(), logAndLat);
        checkInResponseCall.enqueue(new Callback<CheckInResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckInResponse> call,
                                   @NonNull Response<CheckInResponse> response) {
                loading.cancel();
                Log.d(TAG, "onResponse: " + response.toString());
                CheckInResponse check = response.body();
                assert check != null;
                if (check.getStatus() == 800) {
                    showSuccessToast(MiddleSignActivity.this, "签到成功", true, Toast.LENGTH_SHORT);
                    finish();
                } else {
                    showErrorToast(MiddleSignActivity.this, "错误信息：" + check.getMsg(), true,
                            Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckInResponse> call, @NonNull Throwable t) {
                loading.cancel();
                showErrorToast(MiddleSignActivity.this, "网络错误", true, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initSLeave() {
        addView(R.layout.middle_sign_leave);
    }

    private void initUnFinish() {
        addView(R.layout.middle_signunfinish);
    }

    private void initSFinish() {
        // 根据签到标签的不同显示不同的签到完成页面
        switch (mLabel) {
            // 最简单的额，只显示签到完成
            case SNormal:
            case FaceRecognition:
            case SBlueTooth:
            case SFaceLocation:
                finish_Normal();
                break;
            // 显示照片
            case STookPhoto:
                finish_Photo();
                break;
            // 显示地图
            case SLocation:
                finish_MapView();
                break;
            default:
                Log.e(TAG, "initSFinish: switch分支错误，state没有定义");
                break;
        }
    }

    private void finish_MapView() {
        View view = addView(R.layout.middle_signfinish_location);
        AppCompatTextView sflTime = view.findViewById(R.id.middle_details_SFLTime);
        // MapView SFLMap = view.findViewById(R.id.middle_details_SFLMap);
        sflTime.setText(mSign.getFinishTime() + "");
        // 设置地图
        // setMap(SFLMap, sign.getLng(), sign.getLat());
    }

    private void finish_Photo() {
        View view = addView(R.layout.middle_signfinish_photo);
        AppCompatTextView sftpTime = view.findViewById(R.id.middle_details_SFTPTime);
        AppCompatImageView signFinishImage = view.findViewById(R.id.middle_details_signFinishImage);
        sftpTime.setText(mSign.getFinishTime() + "");
        signFinishImage.setImageResource(mSign.getTakePhoto());
    }

    private void finish_Normal() {
        View view = addView(R.layout.middle_signfinish_bluetooth);
        AppCompatTextView sfbTime = view.findViewById(R.id.middle_details_SFBTime);
        sfbTime.setText(mSign.getFinishTime() + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsHaveMapView) {
            // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsHaveMapView) {
            // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mIsHaveMapView) {
            // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mBaiDuMapService.setUNLocationEnabled();
            mLocationService.stop();
            mMapView.onDestroy();
        }
    }

    /**
     * 跳转，开始登录
     *
     * @param context 上下文
     * @param sign    登录所需信息
     */
    public static void startSign(Context context, Sign sign, Uri picUri) {
        Intent intent = new Intent(context, MiddleSignActivity.class);
        intent.putExtra("sign", sign);
        intent.putExtra("picUri", picUri);
        context.startActivity(intent);
    }

    private void showFaceRecognitionFailureDialog(String errorMessage) {
        new AlertDialog.Builder(MiddleSignActivity.this)
                .setTitle("人脸识别失败")
                .setMessage(errorMessage + "\n请返回再次验证")
                .setNegativeButton("好的", (dialog, which) -> finish()).show();
    }
}

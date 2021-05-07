package com.littlecorgi.middle.ui.student;

import static com.littlecorgi.commonlib.util.AndPermissionHelp.andPermission;
import static com.littlecorgi.middle.logic.dao.Tool.SFaceLocation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.littlecorgi.commonlib.AppViewModel;
import com.littlecorgi.commonlib.camerax.CameraActivity;
import com.littlecorgi.commonlib.util.DialogUtil;
import com.littlecorgi.commonlib.util.TimeUtil;
import com.littlecorgi.middle.R;
import com.littlecorgi.middle.logic.CheckOnRepository;
import com.littlecorgi.middle.logic.dao.Tool;
import com.littlecorgi.middle.logic.dao.WindowHelp;
import com.littlecorgi.middle.logic.model.AllCheckOn;
import com.littlecorgi.middle.logic.model.AttendanceBean;
import com.littlecorgi.middle.logic.model.CheckOnDetail;
import com.littlecorgi.middle.logic.model.ClassModel;
import com.littlecorgi.middle.logic.model.Details;
import com.littlecorgi.middle.logic.model.ItemData;
import com.littlecorgi.middle.logic.model.Sign;
import com.littlecorgi.middle.logic.model.TeacherBean;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 学生Fragment
 */
@Route(path = "/middle/fragment_middle_student")
public class MiddleStudentFragment extends Fragment {

    private static final String TAG = "MiddleStudentFragment";
    private View mView;
    private MyAdapter mAdapt;
    private final List<CheckOnDetail> mList = new ArrayList<>();
    private final List<ItemData.AllSignData> mSignList = new ArrayList<>();
    private AppViewModel mViewModel;
    private long studentId;

    ActivityResultLauncher<Intent> mGetContent =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            assert intent != null;
                            Uri picUri = intent.getParcelableExtra("uri");
                            int position = intent.getIntExtra("position", -1);
                            Log.d("MiddleStudentFragment", "从GalleryFragment获取到的图片: " + picUri);

                            if (position == -1) {
                                Toast.makeText(requireContext(), "数据错误", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ItemData.AllSignData itemData = mSignList.get(position);
                            MiddleSignActivity.startSign(
                                    getContext(), convertSignDataToSign(itemData), picUri);
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.middle_studentfragment, container,
                false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        Log.d(TAG, "onViewCreated: " + mViewModel);
        studentId = mViewModel.getStudentId();

        // 提前获取下所需的权限
        andPermission(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        initView();

        if (studentId == -1) {
            Toast.makeText(requireContext(), "未登录或者数据错误", Toast.LENGTH_SHORT).show();
        } else {
            initData();
        }
    }

    private void initView() {
        changeBarColor();
        setRecyclerView();
        initSmartRefreshLayout();
    }

    private void initSmartRefreshLayout() {
        RefreshLayout refreshLayout = mView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(v -> {
            studentId = mViewModel.getStudentId();
            if (studentId == -1) {
                Toast.makeText(requireContext(), "未登录或者数据错误", Toast.LENGTH_SHORT).show();
            } else {
                initData();
            }
            v.finishRefresh(true);
        });
    }

    private void initData() {
        // 模拟的数据
        addList();
        setItemData();
        mAdapt.notifyDataSetChanged();

        getResponseData();
    }

    private void changeBarColor() {
        WindowHelp.setWindowStatusBarColor(getActivity(), R.color.blue);
    }

    private void addList() {
        for (int i = 0; i < 2; i++) {
            CheckOnDetail checkOnDetail = new CheckOnDetail();
            checkOnDetail.setCheckOnStates(0);
            AttendanceBean attendance = new AttendanceBean();
            attendance.setTitle("上课签到");
            attendance.setDescription("上课签到");
            attendance.setStartTime(TimeUtil.INSTANCE.getTimestampFromTime("2021-05-05 09:00:00"));
            attendance.setEndTime(TimeUtil.INSTANCE.getTimestampFromTime("2021-05-05 22:00:00"));
            attendance.setLatitude(34.2212080000);
            attendance.setLongitude(108.9555180000);
            checkOnDetail.setAttendance(attendance);
            TeacherBean teacher = new TeacherBean();
            teacher.setName("翟社平");
            teacher.setAvatar(
                    "https://user-gold-cdn.xitu.io/2018/6/20/1641b2b7bbbd3323?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1");
            ClassModel classModel = new ClassModel();
            classModel.setTeacher(teacher);
            checkOnDetail.getAttendance().setClassDetail(classModel);
            mList.add(checkOnDetail);
        }
        for (int i = 0; i < 2; i++) {
            CheckOnDetail checkOnDetail = new CheckOnDetail();
            checkOnDetail.setCheckOnStates(1);
            AttendanceBean attendance = new AttendanceBean();
            attendance.setTitle("上课签到");
            attendance.setDescription("上课签到");
            attendance.setStartTime(TimeUtil.INSTANCE.getTimestampFromTime("2020-12-27 20:00:00"));
            attendance.setEndTime(TimeUtil.INSTANCE.getTimestampFromTime("2020-12-28 01:34:00"));
            attendance.setLatitude(34.2212080000);
            attendance.setLongitude(108.9555180000);
            checkOnDetail.setAttendance(attendance);
            TeacherBean teacher = new TeacherBean();
            teacher.setName("翟社平");
            teacher.setAvatar(
                    "https://user-gold-cdn.xitu.io/2018/6/20/1641b2b7bbbd3323?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1");
            ClassModel classModel = new ClassModel();
            classModel.setTeacher(teacher);
            checkOnDetail.getAttendance().setClassDetail(classModel);
            mList.add(checkOnDetail);
        }
        for (int i = 0; i < 2; i++) {
            CheckOnDetail checkOnDetail = new CheckOnDetail();
            checkOnDetail.setCheckOnStates(2);
            AttendanceBean attendance = new AttendanceBean();
            attendance.setTitle("随意签");
            attendance.setDescription("随意签");
            attendance.setStartTime(TimeUtil.INSTANCE.getTimestampFromTime("2020-12-27 20:00:00"));
            attendance.setEndTime(TimeUtil.INSTANCE.getTimestampFromTime("2020-1-10 01:34:00"));
            attendance.setLatitude(34.2212080000);
            attendance.setLongitude(108.9555180000);
            checkOnDetail.setAttendance(attendance);
            TeacherBean teacher = new TeacherBean();
            teacher.setName("翟社平");
            teacher.setAvatar(
                    "https://user-gold-cdn.xitu.io/2018/6/20/1641b2b7bbbd3323?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1");
            ClassModel classModel = new ClassModel();
            classModel.setTeacher(teacher);
            checkOnDetail.getAttendance().setClassDetail(classModel);
            mList.add(checkOnDetail);
        }
    }

    private void setItemData() {
        if (mList.size() != 0) {
            mSignList.clear();
            for (CheckOnDetail checkOn : mList) {
                ItemData.AllSignData allSignData = new ItemData.AllSignData();
                allSignData.setCheckOnId(checkOn.getId());
                if (checkOn.getStudent() != null) {
                    allSignData.setStudentId(checkOn.getStudent().getId());
                }
                allSignData.setImage(
                        checkOn.getAttendance().getClassDetail().getTeacher().getAvatar());
                allSignData.setAttendanceId(checkOn.getAttendance().getId());
                allSignData.setTitle(checkOn.getAttendance().getTitle());
                allSignData
                        .setName(checkOn.getAttendance().getClassDetail().getTeacher().getName());
                allSignData.setState(checkOn.getCheckOnStates());
                allSignData.setStartTime(checkOn.getAttendance().getStartTime());
                allSignData.setEndTime(checkOn.getAttendance().getEndTime());
                allSignData.setLat(checkOn.getAttendance().getLatitude());
                allSignData.setLng(checkOn.getAttendance().getLongitude());
                allSignData.setRadius(checkOn.getAttendance().getRadius());
                allSignData.setTheme(checkOn.getAttendance().getTitle());
                allSignData.setState(checkOn.getCheckOnStates());
                allSignData.setLabel(SFaceLocation);
                if (allSignData.getState() == 0) {
                    // 待签到
                    long end = allSignData.getEndTime();
                    long now = new Date().getTime();
                    if (end > now) {
                        allSignData.setMyLabel(Tool.SOG);
                        allSignData.setStateTitle(Tool.SOG_TITLE);
                        allSignData.setLeftColor(
                                getResources().getColor(R.color.Ongoing));
                    } else {
                        allSignData.setMyLabel(Tool.SUnFinish);
                        allSignData.setStateTitle(Tool.SUnFinish_TITLE);
                        allSignData.setLeftColor(
                                getResources().getColor(R.color.warning));
                    }
                } else if (allSignData.getState() == 1) {
                    // 已签
                    allSignData.setMyLabel(Tool.SFinish);
                    allSignData.setStateTitle(Tool.SFinish_TITLE);
                    allSignData.setLeftColor(
                            getResources().getColor(R.color.finish));
                } else if (allSignData.getState() == 2) {
                    // 请假
                    allSignData.setMyLabel(Tool.SLeave);
                    allSignData.setStateTitle(Tool.SLeave_TITLE);
                    allSignData.setLeftColor(
                            getResources().getColor(R.color.leave));
                } else if (allSignData.getState() == 3) {
                    // 签到已结束
                    allSignData.setMyLabel(Tool.SUnFinish);
                    allSignData.setStateTitle(Tool.SUnFinish_TITLE);
                    allSignData.setLeftColor(
                            getResources().getColor(R.color.warning));
                }
                allSignData.setLabelTitle(Tool.getLabelTitle(6));
                mSignList.add(allSignData);
            }
        }
    }

    private void getResponseData() {
        Dialog loading = DialogUtil.writeLoadingDialog(requireContext(), false, "加载数据");
        loading.show();
        loading.setCancelable(false);
        Call<AllCheckOn> allCheckOnCall = CheckOnRepository.getAllCheckOn(studentId);
        allCheckOnCall.enqueue(new Callback<AllCheckOn>() {
            @Override
            public void onResponse(@NonNull Call<AllCheckOn> call,
                                   @NonNull Response<AllCheckOn> response) {
                loading.cancel();
                Log.d("MiddleStudentFragment", "onResponse: " + response.toString());
                assert response.body() != null;
                Log.d("MiddleStudentFragment", "onResponse: " + response.body().toString());
                AllCheckOn allCheckOn = response.body();
                if (allCheckOn.getStatus() == 800) {
                    mList.clear();
                    mList.addAll(allCheckOn.getData());
                    // 模拟的数据
                    addList();
                    setItemData();
                    mAdapt.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "获取数据失败，错误码为" + allCheckOn.getStatus(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllCheckOn> call, @NonNull Throwable t) {
                loading.cancel();
                t.printStackTrace();
                Log.d("MiddleStudentFragment", "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {
        final RecyclerView recyclerView =
                mView.findViewById(R.id.middle_recyclerViewId);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapt = new MyAdapter(R.layout.middle_item_recyclerview, mSignList);
        recyclerView.setAdapter(mAdapt);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 打开详情页面
        mAdapt.setOnItemClickListener(
                (adapter, view, position) -> {
                    Details details = new Details();
                    ItemData.AllSignData itemData = mSignList.get(position);
                    details.setName(itemData.getName());
                    details.setEndTime(itemData.getEndTime());
                    details.setImage(itemData.getImage());
                    details.setStartTime(itemData.getStartTime());
                    details.setLabel(itemData.getLabelTitle());
                    details.setTheme(itemData.getTheme());
                    details.setTitle(itemData.getTitle());
                    MiddleDetailsActivity.startDetails(getContext(), details);
                });
        // 打开签到页面
        mAdapt.addChildClickViewIds(R.id.middle_item_sign);
        mAdapt.setOnItemChildClickListener(
                (adapter, view, position) -> {
                    if (mSignList.get(position).getCheckOnId() != 0) {
                        // 未签到才跳转
                        if (mSignList.get(position).getState() == 0) {
                            // Pass in the mime type you'd like to allow the user to select
                            // as the input
                            mGetContent.launch(new Intent(requireContext(), CameraActivity.class)
                                    .putExtra("position", position));
                        } else if (mSignList.get(position).getState() == 1) {
                            Toast.makeText(requireContext(), "签到已结束", Toast.LENGTH_SHORT).show();
                        } else if (mSignList.get(position).getState() == 2) {
                            Toast.makeText(requireContext(), "已请假，不用签到", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "本地mock数据，不许跳转", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private Sign convertSignDataToSign(ItemData.AllSignData itemData) {
        Sign sign = new Sign();
        sign.setCheckOnId(itemData.getCheckOnId());
        sign.setStudentId(itemData.getStudentId());
        sign.setAttendanceId(itemData.getAttendanceId());
        sign.setState(itemData.getState());
        sign.setLabel(itemData.getLabel());
        sign.setEndTime(itemData.getEndTime());
        sign.setFinishTime(itemData.getFinishTime());
        sign.setLat(itemData.getLat());
        sign.setLng(itemData.getLng());
        sign.setRadius(itemData.getRadius());
        return sign;
    }
}

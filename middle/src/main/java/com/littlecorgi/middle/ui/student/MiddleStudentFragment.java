package com.littlecorgi.middle.ui.student;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.littlecorgi.middle.R;
import com.littlecorgi.middle.logic.dao.Tool;
import com.littlecorgi.middle.logic.dao.WindowHelp;
import com.littlecorgi.middle.logic.model.Details;
import com.littlecorgi.middle.logic.model.ItemData;
import com.littlecorgi.middle.logic.model.Sign;
import com.littlecorgi.middle.logic.network.RetrofitHelp;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 学生Fragment
 */
public class MiddleStudentFragment extends Fragment {

    //todo 未完成的：网络请求地址

    private View view;
    private MyAdapter adapt;
    private List<ItemData.AllSignData> list;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.middle_studentfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        changeBarColor();
        setRecyclerView();
        initClick();
        initSmartRefreshLayout();
    }

    private void initSmartRefreshLayout() {
        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
        refreshLayout.setOnRefreshListener(
                refreshLayout1 -> {
                    if (initData()) {
                        refreshLayout1.finishRefresh(true);
                    }
                });
    }

    private void initClick() {
        AppCompatTextView returnButton = view.findViewById(R.id.student_returnButton);
        AppCompatTextView history = view.findViewById(R.id.student_History);
        returnButton.setOnClickListener(v -> requireActivity().finish());
        history.setOnClickListener(
                v -> {
                    // 跳转到统计界面
                });
    }

    private boolean initData() {
        // 模拟的数据
        addlist();
        setItemData();
        adapt.notifyDataSetChanged();

        return getResponseData();
    }

    private void changeBarColor() {
        WindowHelp.setWindowStatusBarColor(getActivity(), R.color.blue);
    }

    private void addlist() {
        for (int i = 0; i < 2; i++) {
            ItemData.AllSignData allSignData = new ItemData.AllSignData();
            allSignData.setState(1);
            allSignData.setLabel(6);
            allSignData.setEndTime("2020-12-28 01:34");
            allSignData.setStartTime("2020-12-27 20:00");
            allSignData.setTheme("上课签到");
            allSignData.setLat("34.2212080000");
            allSignData.setIng("108.9555180000");
            list.add(allSignData);
        }
        for (int i = 0; i < 2; i++) {
            ItemData.AllSignData allSignData = new ItemData.AllSignData();
            allSignData.setState(2);
            allSignData.setLabel(6);
            allSignData.setEndTime("2020-1-10 01:34");
            allSignData.setStartTime("2020-12-27 20:00");
            allSignData.setTheme("随意签");
            allSignData.setLat("34.2212080000");
            allSignData.setIng("108.9555180000");
            list.add(allSignData);
        }
        for (int i = 0; i < 2; i++) {
            ItemData.AllSignData allSignData = new ItemData.AllSignData();
            allSignData.setState(2);
            allSignData.setLabel(6);
            allSignData.setEndTime("2021-1-10 01:34");
            allSignData.setStartTime("2020-12-27 20:00");
            allSignData.setTheme("随意签");
            allSignData.setLat("34.161057");
            allSignData.setIng("108.912334");
            list.add(allSignData);
        }
        for (int i = 0; i < 2; i++) {
            ItemData.AllSignData allSignData = new ItemData.AllSignData();
            allSignData.setState(2);
            allSignData.setLabel(6);
            allSignData.setEndTime("2020-12-28 01:34");
            allSignData.setStartTime("2020-12-27 20:00");
            allSignData.setTheme("随意签");
            allSignData.setLat("34.2212080000");
            allSignData.setIng("108.9555180000");
            list.add(allSignData);
        }

        for (int i = 0; i < 2; i++) {
            ItemData.AllSignData allSignData = new ItemData.AllSignData();
            allSignData.setState(3);
            allSignData.setLabel(6);
            allSignData.setEndTime("2020-12-28 01:34");
            allSignData.setStartTime("2020-12-27 20:00");
            allSignData.setTheme("随意签");
            allSignData.setLat("34.2212080000");
            allSignData.setIng("108.9555180000");
            list.add(allSignData);
        }
    }
    private void setItemData() {
        if (list.size() != 0) {
            for (ItemData.AllSignData allSignData : list) {
                if (allSignData.getState() == 2) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    long end = 0;
                    try {
                        end = Objects.requireNonNull(simpleDateFormat.parse(allSignData.getEndTime())).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long now = new Date().getTime();
                    if (end > now) {
                        allSignData.setMyLabel(Tool.SOG);
                        allSignData.setStateTitle(Tool.SOG_TITLE);
                        allSignData.setLeftColor(getResources().getColor(R.color.Ongoing));
                    } else {
                        allSignData.setMyLabel(Tool.SUnFinish);
                        allSignData.setStateTitle(Tool.SUnFinish_TITLE);
                        allSignData.setLeftColor(getResources().getColor(R.color.warning));
                    }
                } else if (allSignData.getState() == 1) {
                    allSignData.setMyLabel(Tool.SFinish);
                    allSignData.setStateTitle(Tool.SFinish_TITLE);
                    allSignData.setLeftColor(getResources().getColor(R.color.finish));
                } else if (allSignData.getState() == 3) {
                    allSignData.setMyLabel(Tool.SLeave);
                    allSignData.setStateTitle(Tool.SLeave_TITLE);
                    allSignData.setLeftColor(getResources().getColor(R.color.leave));
                }
                allSignData.setLabelTitle(Tool.getLabelTitle(allSignData.getLabel()));
            }
        }
    }
    private boolean getResponseData() {
        final boolean[] isSuccess = new boolean[1];
        Call<ItemData> call = RetrofitHelp.getAllSign();
        call.enqueue(
                new Callback<ItemData>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<ItemData> call, @NotNull Response<ItemData> response) {
                        if (response.body() != null) {
                            list.clear();
                            list.addAll(response.body().getAllSignData());
                            addlist();
                            setItemData();
                            adapt.notifyDataSetChanged();
                            isSuccess[0] = true;
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ItemData> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), "网络连接失败，过会在试吧", Toast.LENGTH_LONG).show();
                        isSuccess[0] = false;
                    }
                });
        return isSuccess[0];
    }
    private void setRecyclerView() {
        final RecyclerView recyclerView = view.findViewById(R.id.middle_recyclerViewId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list = new ArrayList<>();
        adapt = new MyAdapter(R.layout.middle_item_recyclerview, list);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 打开详情页面
        adapt.setOnItemClickListener(
                (adapter, view, position) -> {
                    Details details = new Details();
                    ItemData.AllSignData itemData = list.get(position);
                    details.setName(itemData.getName());
                    details.setEndTime(itemData.getEndTime());
                    details.setImage(itemData.getImage());
                    details.setStartTime(itemData.getStartTime());
                    details.setLabel(itemData.getLabelTitle());
                    details.setTheme(itemData.getTheme());
                    details.setOccupational(itemData.getOccupational());
                    details.setTitle(itemData.getTitle());
                    MiddleDetailsActivity.startDetails(getContext(), details);
                });
        // 打开签到页面
        adapt.addChildClickViewIds(R.id.middle_item_sign);
        adapt.setOnItemChildClickListener(
                (adapter, view, position) -> {
                    ItemData.AllSignData itemData = list.get(position);
                    Sign sign = new Sign();
                    sign.setState(itemData.getMyLabel());
                    sign.setLabel(itemData.getLabel());
                    sign.setEndTime(itemData.getEndTime());
                    sign.setFinishTime(itemData.getFinishTime());
                    // sign.setTakePhoto(itemData.getBgImage());
                    sign.setLat(itemData.getLat());
                    sign.setLng(itemData.getIng());
                    MiddleSignActivity.startSign(getContext(), sign);
                });
    }
}

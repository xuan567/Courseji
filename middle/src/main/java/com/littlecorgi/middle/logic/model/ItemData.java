package com.littlecorgi.middle.logic.model;

import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这个类即是接收网络请求返回学生数据的类，也是RecyclerView对应的数据类
 */
public class ItemData {

    @SerializedName("allSignData")
    private List<AllSignData> allSignData;

    public List<AllSignData> getAllSignData() {
        return allSignData;
    }

    public void setAllSignData(List<AllSignData> allSignData) {
        this.allSignData = allSignData;
    }

    /**
     * 登录信息
     */
    @Data
    @NoArgsConstructor
    public static class AllSignData {

        private String stateTitle; // 文字状态
        private String labelTitle;
        private int leftColor; // 左边框的颜色
        private int myLabel; // 应该是myState写错了

        @SerializedName("CheckOnId")
        private long checkOnId; // 签到id

        @SerializedName("StudentId")
        private long studentId; // 签到id

        @SerializedName("AttendanceId")
        private long attendanceId; // 签到id

        @SerializedName("Theme")
        private String theme; // 主题

        @SerializedName("StartTime")
        private long startTime; // 开始时间

        @SerializedName("EndTime")
        private long endTime; // 结束时间

        @SerializedName("State")
        private int state; // 签到状态

        @SerializedName("Label")
        private int label; // 标签

        @SerializedName("Name")
        private String name; // 老师的名字

        @SerializedName("image")
        private String image; // 老师的图像

        @SerializedName("Title")
        private String title; // 签到的内容

        @SerializedName("FinishTime")
        private long finishTime; // 完成签到的时间

        @SerializedName("Lat")
        private double lat; // 纬度

        @SerializedName("Lng")
        private double lng; // 经度

        @SerializedName("Radius")
        private int radius; // 签到范围

        @SerializedName("signPhoto")
        private File signPhoto; // 如果是拍照签到需要返回图片
    }
}

package com.littlecorgi.middle.logic.model;

import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.util.List;

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
    public static class AllSignData {

        private String stateTitle; // 文字状态
        private String labelTitle;
        private int leftColor; // 左边框的颜色
        private int myLabel; // 应该是myState写错了

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

        @SerializedName("Occupational")
        private String occupational; // 老师的职业

        @SerializedName("image")
        private File image; // 老师的图像

        @SerializedName("Title")
        private String title; // 签到的内容

        @SerializedName("FinishTime")
        private long finishTime; // 完成签到的时间

        @SerializedName("Lat")
        private String lat; // 纬度

        @SerializedName("Ing")
        private String ing; // 经度

        @SerializedName("signPhoto")
        private File signPhoto; // 如果是拍照签到需要返回图片

        public String getStateTitle() {
            return stateTitle;
        }

        public void setStateTitle(String stateTitle) {
            this.stateTitle = stateTitle;
        }

        public String getLabelTitle() {
            return labelTitle;
        }

        public void setLabelTitle(String labelTitle) {
            this.labelTitle = labelTitle;
        }

        public int getLeftColor() {
            return leftColor;
        }

        public void setLeftColor(int leftColor) {
            this.leftColor = leftColor;
        }

        public int getMyLabel() {
            return myLabel;
        }

        public void setMyLabel(int myLabel) {
            this.myLabel = myLabel;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getLabel() {
            return label;
        }

        public void setLabel(int label) {
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOccupational() {
            return occupational;
        }

        public void setOccupational(String occupational) {
            this.occupational = occupational;
        }

        public File getImage() {
            return image;
        }

        public void setImage(File image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getIng() {
            return ing;
        }

        public void setIng(String ing) {
            this.ing = ing;
        }

        public File getSignPhoto() {
            return signPhoto;
        }

        public void setSignPhoto(File signPhoto) {
            this.signPhoto = signPhoto;
        }
    }
}

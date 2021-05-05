package com.littlecorgi.middle.logic.dao;


import java.util.ArrayList;
import java.util.List;

/**
 * 状态
 */
public class Tool {

    // 签到状态 state
    public static final int SOG = 0; // 签到进行中
    public static final int SFinish = 1; // 完成签到
    public static final int SLeave = 2; // 已请假
    public static final int SUnFinish = 3; // 签到已结束，未签到

    // 状态标题
    public static final String SOG_TITLE = "进行中";
    public static final String SFinish_TITLE = "已完成";
    public static final String SUnFinish_TITLE = "未签到";
    public static final String SLeave_TITLE = "已请假";

    // 签到的标签 label
    public static final int SNormal = 1; // 普通签到
    public static final int FaceRecognition = 2; // 人脸识别,活体检测
    public static final int STookPhoto = 3; // 拍照签到
    public static final int SBlueTooth = 4; // 蓝牙签到
    public static final int SLocation = 5; // 定位签到
    public static final int SFaceLocation = 6; // 人脸识别,活体检测+定位
    public static final int SBlueToothLocation = 7; // 蓝牙,人脸识别,活体检测

    /**
     * 获取传入的时间对应的00:00的毫秒值
     */
    public static long getZeroMilliseconds(long time) {
        return (time / (1000 * 60 * 60 * 24)) * (1000 * 60 * 60 * 24);
    }

    private static final String[] Label = {"上课签到", "早操签到", "自习签到", "到校签到", "放假签到", "随意签"};

    public static String[] getLabel() {
        return Label;
    }

    /**
     * 返回主题列表
     */
    public static List<String> getThemeList() {
        List<String> list = new ArrayList<>();
        list.add("上课签到");
        list.add("早操签到");
        list.add("自习签到");
        list.add("到校签到");
        list.add("放假签到");
        list.add("随意签");
        return list;
    }

    /**
     * 返回标签列表
     *
     * @param index 索引
     * @return index对应的标签
     */
    public static String getLabelTitle(int index) {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("普通签到");
        list.add("人脸识别,活体检测");
        list.add("拍照签到");
        list.add("蓝牙签到");
        list.add("定位签到");
        list.add("人脸识别,活体检测 定位签到");
        list.add("人脸识别,活体检测 蓝牙签到");
        return list.get(index);
    }
}

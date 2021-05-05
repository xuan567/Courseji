package com.littlecorgi.middle.logic.model;

import java.io.Serializable;

/**
 * 登录信息
 */
public class Sign implements Serializable {

    private int state;
    private int label;
    private int takePhoto;
    private long finishTime;
    private long endTime;
    private String lat;
    private String lng;

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

    public int getTakePhoto() {
        return takePhoto;
    }

    public void setTakePhoto(int takePhoto) {
        this.takePhoto = takePhoto;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}

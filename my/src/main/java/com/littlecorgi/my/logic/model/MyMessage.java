package com.littlecorgi.my.logic.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 我的信息数据bean
 */
public class MyMessage implements Serializable {

    private static final long serialVersionUID = 1234567890604L;

    private String picture;

    private int myImage; // 图片

    private String imagePath; // 图像路径

    private String name; // 名字

    private String id; // 身份证

    private String gender; // 性别

    private String professional; // 专业

    private String describe; // 描述

    private String national; // 民族

    private Map<Integer, String> myClass; // 班级

    private String version; // 版本号

    private String update; // 下载地址

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMyImage() {
        return myImage;
    }

    public void setMyImage(int myImage) {
        this.myImage = myImage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public Map<Integer, String> getMyClass() {
        return myClass;
    }

    public void setMyClass(Map<Integer, String> myClass) {
        this.myClass = myClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}

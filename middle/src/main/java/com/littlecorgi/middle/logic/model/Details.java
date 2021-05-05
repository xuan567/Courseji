package com.littlecorgi.middle.logic.model;

import java.io.File;
import java.io.Serializable;

/**
 *
 */
public class Details implements Serializable {

    private String theme;
    private String title;
    private String label;
    private long startTime;
    private long endTime;
    private File image;
    private String name;
    private String occupational;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
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
}

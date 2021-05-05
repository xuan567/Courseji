package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;

/**
 *
 */
@Data
public class Details implements Serializable {

    private String theme;
    private String title;
    private String label;
    private long startTime;
    private long endTime;
    private String image;
    private String name;
}

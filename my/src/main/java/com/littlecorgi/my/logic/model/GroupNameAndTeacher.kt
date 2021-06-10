package com.littlecorgi.my.logic.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author littlecorgi 2021/5/4
 */
@Data
public class GroupNameAndTeacher implements Serializable {
    private static final long serialVersionUID = 1234567890606L;
    long id;
    String name;
    String teacherName;
}

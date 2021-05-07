package com.littlecorgi.leave.logic.model;

import java.io.Serializable;

/**
 * 班级数据类
 *
 * @author littlecorgi 2021/5/5
 */
@lombok.NoArgsConstructor
@lombok.Data
public class ClassDetailBean implements Serializable {
    private static final long serialVersionUID = 1234567890402L;
    private int id;
    private String name;
    private TeacherBean teacher;
}

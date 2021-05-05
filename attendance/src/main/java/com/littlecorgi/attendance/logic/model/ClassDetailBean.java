package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班级的model
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class ClassDetailBean implements Serializable {
    private static final long serialVersionUID = 1234567890204L;
    private int id;
    private String name;
    private TeacherBean teacher;
}

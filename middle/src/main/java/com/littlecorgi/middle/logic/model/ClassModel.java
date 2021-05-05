package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应的班级信息
 *
 * @author littlecorgi 2021/5/5
 */
@Data
@NoArgsConstructor
public class ClassModel implements Serializable {
    private static final long serialVersionUID = 1234567890505L;
    private long id;
    private String name;
    private TeacherBean teacher;
}

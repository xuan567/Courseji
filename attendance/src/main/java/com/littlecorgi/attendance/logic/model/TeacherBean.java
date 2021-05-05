package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师的model
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class TeacherBean implements Serializable {
    private static final long serialVersionUID = 1234567890206L;
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatar;
}

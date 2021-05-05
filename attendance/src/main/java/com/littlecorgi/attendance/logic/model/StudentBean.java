package com.littlecorgi.attendance.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生的model
 *
 * @author littlecorgi 2021/5/5
 */
@NoArgsConstructor
@Data
public class StudentBean implements Serializable {
    private static final long serialVersionUID = 1234567890205L;
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private String picture;
}
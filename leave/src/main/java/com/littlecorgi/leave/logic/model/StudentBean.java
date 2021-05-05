package com.littlecorgi.leave.logic.model;

import java.io.Serializable;

/**
 * 学生数据类
 *
 * @author littlecorgi 2021/5/5
 */
@lombok.NoArgsConstructor
@lombok.Data
public class StudentBean implements Serializable {
    private static final long serialVersionUID = 1234567890404L;
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private String picture;
}

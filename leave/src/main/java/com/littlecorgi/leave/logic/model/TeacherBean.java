package com.littlecorgi.leave.logic.model;

/**
 * 教师数据类
 *
 * @author littlecorgi 2021/5/5
 */
@lombok.NoArgsConstructor
@lombok.Data
public class TeacherBean {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatar;
}

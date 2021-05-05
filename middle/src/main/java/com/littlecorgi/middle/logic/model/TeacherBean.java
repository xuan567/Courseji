package com.littlecorgi.middle.logic.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应的教师信息
 *
 * @author littlecorgi 2021/5/5
 */
@Data
@NoArgsConstructor
public class TeacherBean implements Serializable {
    private static final long serialVersionUID = 1234567890506L;
    private long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatar;
}

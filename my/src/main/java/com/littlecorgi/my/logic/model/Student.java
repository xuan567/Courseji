package com.littlecorgi.my.logic.model;

/**
 * 学生实体类
 *
 * @author littlecorgi 2021/05/03
 */
@lombok.NoArgsConstructor
@lombok.Data
public class Student {

    private Integer status;
    private String msg;
    private String errorMsg;
    private DataBean data;

    /**
     *
     */
    @lombok.NoArgsConstructor
    @lombok.Data
    public static class DataBean {
        private long id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private String avatar;
        private String picture;
    }
}
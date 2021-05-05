package com.littlecorgi.leave.logic.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取学生加入的所有的班级
 *
 * @author littlecorgi 2021/5/4
 */
@NoArgsConstructor
@Data
public class AllClassResponse implements Serializable {
    private static final long serialVersionUID = 1234567890607L;
    private Integer status;
    private String msg;
    private String errorMsg;
    private List<DataBean> data;

    /**
     * 具体Class信息
     */
    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 1234567890608L;
        private Integer id;
        private String name;
        private TeacherBean teacher;

        /**
         * Class对应的Teacher信息
         */
        @NoArgsConstructor
        @Data
        public static class TeacherBean implements Serializable {
            private static final long serialVersionUID = 1234567890609L;
            private Integer id;
            private String name;
            private String email;
            private String password;
            private String phone;
            private String avatar;
        }
    }
}

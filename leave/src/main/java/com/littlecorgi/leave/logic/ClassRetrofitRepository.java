package com.littlecorgi.leave.logic;

import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.leave.logic.model.AllClassResponse;
import com.littlecorgi.leave.logic.net.ClassInterface;
import retrofit2.Call;

/**
 * 班级 Retrofit封装类
 *
 * @author littlecorgi 2021/5/4
 */
public class ClassRetrofitRepository {

    /**
     * 获取这个学生加入的所有班级
     *
     * @param studentId 学生id
     */
    public static Call<AllClassResponse> getAllClassFromTheStudentCall(long studentId) {
        ClassInterface classInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(ClassInterface.class);
        // 执行请求
        return classInterface.getAllClassFromTheStudent(studentId);
    }
}

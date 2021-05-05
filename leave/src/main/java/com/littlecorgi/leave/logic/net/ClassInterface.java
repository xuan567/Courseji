package com.littlecorgi.leave.logic.net;

import com.littlecorgi.leave.logic.model.AllClassResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/5
 */
public interface ClassInterface {

    /**
     * 获取该学生加入的所有班级
     *
     * @param studentId 学生id
     */
    @GET("/classAndStudent/getAllClassFromTheStudent")
    Call<AllClassResponse> getAllClassFromTheStudent(@Query("studentId") long studentId);

}

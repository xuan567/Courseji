package com.littlecorgi.my.logic.network;

import com.littlecorgi.my.logic.model.AllClassResponse;
import com.littlecorgi.my.logic.model.JoinClassResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/4
 */
public interface ClassInterface {

    /**
     * 获取该学生加入的所有班级
     *
     * @param studentId 学生id
     */
    @GET("/classAndStudent/getAllClassFromTheStudent")
    Call<AllClassResponse> getAllClassFromTheStudent(@Query("studentId") long studentId);

    /**
     * 加入课程
     *
     * @param studentId 学生id
     * @param classId   班级id
     */
    @POST("/classAndStudent/joinClass")
    Call<JoinClassResponse> joinClass(@Query("studentId") long studentId,
                                      @Query("classId") long classId);
}

package com.littlecorgi.middle.logic.network;

import com.littlecorgi.middle.logic.model.AllCheckOn;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/5
 */
public interface CheckOnInterface {

    /**
     * 获取这名学生的所有签到
     *
     * @param studentId 学生id
     */
    @POST("/checkOn/getTheStudentAllCheckInInfo")
    Call<AllCheckOn> getTheStudentAllCheckInInfo(@Query("studentId") long studentId);
}

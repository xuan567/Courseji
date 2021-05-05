package com.littlecorgi.attendance.logic.net;

import com.littlecorgi.attendance.logic.model.AllCheckOnResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/5
 */
public interface AttendanceInterface {

    /**
     * 获取这名学生所有课的签到纪录
     *
     * @param studentId 学生id
     */
    @POST("/checkOn/getTheStudentAllCheckInInfo")
    Call<AllCheckOnResponse> getAllCheckOn(@Query("studentId") long studentId);
}

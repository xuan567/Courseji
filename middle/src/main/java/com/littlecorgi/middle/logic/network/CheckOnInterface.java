package com.littlecorgi.middle.logic.network;

import com.littlecorgi.middle.logic.model.AllCheckOn;
import com.littlecorgi.middle.logic.model.CheckInResponse;
import com.littlecorgi.middle.logic.model.LogAndLat;
import retrofit2.Call;
import retrofit2.http.Body;
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

    /**
     * 参与签到
     *
     * @param attendanceId 考勤的id
     * @param studentId    学生的id
     * @param logAndLat    定位信息
     */
    @POST("/checkOn/checkIn")
    Call<CheckInResponse> checkIn(@Query("attendanceId") long attendanceId,
                                  @Query("studentId") long studentId,
                                  @Body LogAndLat logAndLat);
}

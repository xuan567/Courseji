package com.littlecorgi.leave.logic;

import com.littlecorgi.leave.logic.model.AllLeaveResponse;
import com.littlecorgi.leave.logic.model.CreateLeaveResponse;
import com.littlecorgi.leave.logic.model.GetLeaveResponse;
import com.littlecorgi.leave.logic.model.LeaveRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/5
 */
interface LeaveInterface {

    @POST("/leave/getLeaveFromStudent")
    Call<AllLeaveResponse> getLeaveFromStudent(@Query("studentId") long studentId);

    @POST("/leave/getLeave")
    Call<GetLeaveResponse> getLeaveInfo(@Query("leaveId") long leaveId);

    @POST("/leave/createLeave")
    Call<CreateLeaveResponse> createLeave(@Query("studentId") long studentId,
                                          @Query("classId") long classId,
                                          @Body LeaveRequest leave);
}

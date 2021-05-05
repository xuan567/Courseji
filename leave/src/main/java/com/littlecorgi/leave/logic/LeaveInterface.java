package com.littlecorgi.leave.logic;

import com.littlecorgi.leave.logic.model.AllLeaveResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author littlecorgi 2021/5/5
 */
interface LeaveInterface {

    @POST("/leave/getLeaveFromStudent")
    Call<AllLeaveResponse> getLeaveFromStudent(@Query("studentId") long studentId);
}

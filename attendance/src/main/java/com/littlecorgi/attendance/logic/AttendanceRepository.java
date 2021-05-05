package com.littlecorgi.attendance.logic;

import com.littlecorgi.attendance.logic.model.AllCheckOnResponse;
import com.littlecorgi.attendance.logic.net.AttendanceInterface;
import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import retrofit2.Call;

/**
 * @author littlecorgi 2021/5/5
 */
public class AttendanceRepository {

    private static AttendanceInterface getInterface() {
        return TencentServerRetrofitKt.getTencentCloudRetrofit().create(AttendanceInterface.class);
    }

    public static Call<AllCheckOnResponse> getAllCheckOn(long studentId) {
        return getInterface().getAllCheckOn(studentId);
    }
}

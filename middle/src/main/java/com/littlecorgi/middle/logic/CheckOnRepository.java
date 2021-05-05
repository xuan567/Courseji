package com.littlecorgi.middle.logic;

import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.middle.logic.model.AllCheckOn;
import com.littlecorgi.middle.logic.model.CheckInResponse;
import com.littlecorgi.middle.logic.model.LogAndLat;
import com.littlecorgi.middle.logic.network.CheckOnInterface;
import retrofit2.Call;

/**
 * @author littlecorgi 2021/5/5
 */
public class CheckOnRepository {

    private static CheckOnInterface getCheckOnInterface() {
        return TencentServerRetrofitKt.getTencentCloudRetrofit().create(CheckOnInterface.class);
    }

    /**
     * 获取这名学生所有的签到信息
     *
     * @param studentId 学生id
     */
    public static Call<AllCheckOn> getAllCheckOn(long studentId) {

        return getCheckOnInterface().getTheStudentAllCheckInInfo(studentId);
    }

    public static Call<CheckInResponse> checkIn(long studentId,
                                                long attendanceId,
                                                LogAndLat logAndLat) {
        return getCheckOnInterface().checkIn(attendanceId, studentId, logAndLat);
    }
}

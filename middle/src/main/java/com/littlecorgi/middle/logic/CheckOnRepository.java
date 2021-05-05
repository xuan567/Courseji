package com.littlecorgi.middle.logic;

import com.littlecorgi.commonlib.logic.TencentServerRetrofitKt;
import com.littlecorgi.middle.logic.model.AllCheckOn;
import com.littlecorgi.middle.logic.network.CheckOnInterface;
import retrofit2.Call;

/**
 * @author littlecorgi 2021/5/5
 */
public class CheckOnRepository {

    /**
     * 获取这名学生所有的签到信息
     *
     * @param studentId 学生id
     */
    public static Call<AllCheckOn> getAllCheckOn(long studentId) {
        CheckOnInterface checkOnInterface =
                TencentServerRetrofitKt.getTencentCloudRetrofit()
                        .create(CheckOnInterface.class);
        return checkOnInterface.getTheStudentAllCheckInInfo(studentId);
    }
}

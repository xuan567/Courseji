package com.littlecorgi.middle.logic.network;

import com.littlecorgi.middle.logic.model.ItemData;
import com.littlecorgi.middle.logic.model.SignResult;
import java.io.File;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit网络请求接口.
 */
public interface GetRequestInterface {

    // Retrofit中用于描述网络请求的接口

    // 老师发起签到：

    // @FieldMap与@Field的作用一致，可以用于添加多个不确定的参数，类似@QueryMap，Map的key作为表单的键，Map的value作为表单的值
    @FormUrlEncoded
    @POST("user/emails")
    Call<ResponseBody> signIn(@FieldMap Map<String, Object> map);

    // 学生返回签到情况

    @GET("user")
    Call<SignResult> getStudentSign(@Query("image") File file);

    // 接收学生的签到情况
    @GET("user")
    Call<ItemData> getAllSign();
}

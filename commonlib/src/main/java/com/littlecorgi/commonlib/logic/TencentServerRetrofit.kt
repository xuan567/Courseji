package com.littlecorgi.commonlib.logic

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 腾讯云服务器81.71.76.214的Retrofit实例
 *
 * @author littlecorgi 2021/5/2
 */

private var retrofit: Retrofit? = null

fun getTencentCloudRetrofit(): Retrofit {
    if (retrofit == null) {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(
                "TencentServerRetrofit",
                "log: retrofitBack = $message"
            )
        }
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("http://81.71.76.213:10800")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    return retrofit!!
}
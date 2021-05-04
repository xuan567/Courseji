package com.littlecorgi.commonlib.uploadfile.logic

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 *
 * @author littlecorgi 2021/5/3
 */
internal interface FileInterface {
    /**
     * 上传图片文件
     */
    @Multipart
    @POST("/file/uploadPicture")
    fun uploadPicFile(@Part picFile: MultipartBody.Part): Call<UploadFileResponse>
}
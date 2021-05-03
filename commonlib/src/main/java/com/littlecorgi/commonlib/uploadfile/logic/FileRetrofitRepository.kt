package com.littlecorgi.commonlib.uploadfile.logic

import com.littlecorgi.commonlib.logic.getTencentCloudRetrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

/**
 *
 * @author littlecorgi 2021/5/3
 */
object FileRetrofitRepository {

    fun getUploadCall(file: File): Call<UploadFileResponse> {
        // 创建 RequestBody，用于封装构建RequestBody
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        val body = MultipartBody.Part.createFormData("picFile", file.name, requestFile)

        val fileInterface: FileInterface = getTencentCloudRetrofit()
            .create(FileInterface::class.java)
        // 执行请求
        return fileInterface
            .uploadPicFile(body)
    }
}

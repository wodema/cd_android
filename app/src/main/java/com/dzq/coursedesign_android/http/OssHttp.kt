package com.dzq.coursedesign_android.http

import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.utils.GsonUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object OssHttp {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.1.104/oss/fileUpload"

    fun upload(photoPath: String?, handler: Handler) {
        val file = File(photoPath!!)
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", file.name,
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL)
            .post(body)
            .build()

        val newCall = client.newCall(request)
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                handler.sendMessage(Message().apply {
                    what = -1
                    obj = Result(code = -1, message = "网络异常", data = null)
                })
            }
            override fun onResponse(call: Call, response: Response) {
                val result = GsonUtil.fromJson<Result>(response.body?.string())
                val message = Message().apply {
                    what = result.code
                    obj = result
                }
                handler.sendMessage(message)
            }
        })
    }
}
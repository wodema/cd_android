package com.dzq.coursedesign_android.http

import android.os.Handler
import android.os.Message
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.utils.GsonUtil
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

object StudentHttp {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.1.104/student"

    fun smsSignin(mobile: String, authCode: String, handler: Handler) {
        val body = FormBody.Builder()
            .add("mobile", mobile)
            .add("authCode", authCode)
            .build()

        val request: Request = Request.Builder()
            .url("$BASE_URL/sms/signin")
            .post(body)
            .build()
        return client.newCall(request).enqueue(
            object : Callback {
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
            }
        )
    }

    fun signin(userEmail: String, password: String): Response {
        val body = FormBody.Builder()
            .add("email", userEmail)
            .add("password", password)
            .build()

        val request: Request = Request.Builder()
            .url("$BASE_URL/signin")
            .post(body)
            .build()
        return client.newCall(request).execute()
    }
}
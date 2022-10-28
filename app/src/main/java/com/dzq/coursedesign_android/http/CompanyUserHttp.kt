package com.dzq.coursedesign_android.http

import android.os.Handler
import android.os.Message
import com.dzq.coursedesign_android.entity.Result
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

object CompanyUserHttp {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val BASE_URL = "http://192.168.1.104/company/user"

    fun smsSignin(mobile: String, authCode: String, handler: Handler) {
        val body = FormBody.Builder()
            .add("mobile", mobile)
            .add("authCode", authCode)
            .build()

        val request: Request = Request.Builder()
            .url("$BASE_URL/sms/signin")
            .post(body)
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                   e.printStackTrace()
                    handler.sendMessage(Message().apply {
                        what = -1
                    })
                }
                override fun onResponse(call: Call, response: Response) {
                    val result = gson.fromJson(response.body?.string(), Result::class.java)
                    handler.sendMessage(Message().apply {
                        what = result.code
                        obj = result.data
                    })
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
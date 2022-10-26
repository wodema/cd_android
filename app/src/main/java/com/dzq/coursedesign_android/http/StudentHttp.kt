package com.dzq.coursedesign_android.http

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

object StudentHttp {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.1.104/student"

    fun smsSignin(mobile: String, authCode: String): Response {
        val body = FormBody.Builder()
            .add("mobile", mobile)
            .add("authCode", authCode)
            .build()

        val request: Request = Request.Builder()
            .url("$BASE_URL/sms/signin")
            .post(body)
            .build()
        return client.newCall(request).execute()
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
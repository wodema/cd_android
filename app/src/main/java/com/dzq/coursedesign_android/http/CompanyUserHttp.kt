package com.dzq.coursedesign_android.http

import android.os.Handler
import android.os.Message
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.utils.GsonUtil
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object CompanyUserHttp {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

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
                        obj = Result(code = -1, message = "网络异常", data = null)
                    })
                }
                override fun onResponse(call: Call, response: Response) {
                    val result = GsonUtil.fromJson<Result>(response.body?.string())
                    handler.sendMessage(Message().apply {
                        what = result.code
                        obj = result
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

    fun bindMobile(id: Int, toString: String, bindMobileHandler: Handler) {
        val body = FormBody.Builder()
            .add("id", id.toString())
            .add("mobile", toString)
            .build()

        val request: Request = Request.Builder()
            .url("$BASE_URL/bind/mobile")
            .post(body)
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    bindMobileHandler.sendMessage(Message().apply {
                        what = -1
                        obj = Result(code = -1, message = "网络异常", data = null)
                    })
                }
                override fun onResponse(call: Call, response: Response) {
                    val result = GsonUtil.fromJson<Result>(response.body?.string())
                    bindMobileHandler.sendMessage(Message().apply {
                        what = result.code
                        obj = result
                    })
                }
            }
        )
    }

    fun authentication(companyUser: CompanyUser, handler: Handler) {
        val requestBody =
            GsonUtil.objToJson(companyUser).toRequestBody("application/json".toMediaTypeOrNull())
        val request: Request = Request.Builder()
            .url("$BASE_URL/authentication")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(
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
                    handler.sendMessage(Message().apply {
                        what = result.code
                        obj = result
                    })
                }
            }
        )
    }


}
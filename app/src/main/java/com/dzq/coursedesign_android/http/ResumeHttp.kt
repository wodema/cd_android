package com.dzq.coursedesign_android.http

import android.os.Handler
import android.os.Message
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.entity.Student
import com.dzq.coursedesign_android.utils.GsonUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object ResumeHttp {


    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val BASE_URL = "http://192.168.1.104/resume"

    fun applyPosition(
        companyPosition: CompanyPosition,
        student: Student,
        handler: Handler
    ) {
        val request: Request
        val requestBody = GsonUtil.objToJson(companyPosition)
            .toRequestBody("application/json".toMediaTypeOrNull())
        request = Request.Builder()
            .url("$BASE_URL?studentId=${student.id}")
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
                    val message = Message().apply {
                        what = result.code
                        obj = result
                    }
                    handler.sendMessage(message)
                }
            }
        )
    }

    fun getCompanyResumeList(companyId: Int, handler: Handler) {
        val request: Request = Request.Builder()
            .url("$BASE_URL/company?companyId=$companyId")
            .get()
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
                    val message = Message().apply {
                        what = result.code
                        obj = result
                    }
                    handler.sendMessage(message)
                }
            }
        )
    }

    fun getStudentResumeList(studentId: Int, handler: Handler) {
        val request: Request = Request.Builder()
            .url("$BASE_URL/student?studentId=$studentId")
            .get()
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
                    val message = Message().apply {
                        what = result.code
                        obj = result
                    }
                    handler.sendMessage(message)
                }
            }
        )
    }
}
package com.dzq.coursedesign_android

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyUserHttp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var mobileEditText: EditText
    private lateinit var authCodeEditText: EditText
    private val gson = Gson()

    private val companyUserLoginHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    Log.e("Kotlin", "接收通过sendEmptyMessageDelayed()发送过来的消息")
                    Log.d("aaa", msg.obj.toString())
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录成功:${result}", Toast.LENGTH_SHORT).show()
                    val sp = getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    val user = gson.fromJson(gson.toJson(result.data), CompanyUser::class.java)
                    sp.edit().apply {
                        putInt("id", user.id)
                        putString("username", user.userName)
                        putInt("companyId", user.companyId)
                        putString("userMobile", user.userMobile)
                        putString("userAvatar", user.userAvatar)
                        putString("userIdcard", user.idCard)
                        putInt("authStatus", user.authStatus)
                        apply()
                    }
                }
                // 这里的else相当于Java中switch的default;
                else -> {
                    Log.e("asaa", msg.toString())
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录异常:${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_login)
        initView()
    }

    private fun initView() {
        loginButton = findViewById(R.id.btn_company_login)
        mobileEditText = findViewById(R.id.edit_company_mobile)
        authCodeEditText = findViewById(R.id.edit_company_authCode)
        loginButton.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            val authCode = authCodeEditText.text.toString()
            CompanyUserHttp.smsSignin(mobile = mobile, authCode = authCode, handler = companyUserLoginHandler)
        }
    }
}
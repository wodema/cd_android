package com.dzq.coursedesign_android.company

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyUserHttp
import com.dzq.coursedesign_android.http.SmsHttp
import com.google.gson.Gson

class CompanyLoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var smsButton: Button
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
                        putString("data", gson.toJson(user))
                        apply()
                    }
                    Intent(applicationContext, CompanyMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(this)
                    }
                }
                // 这里的else相当于Java中switch的default;
                else -> {
                    Log.e("asaa", msg.toString())
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录异常:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val smsHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    Toast.makeText(applicationContext, "发送成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "发送异常:${result.message }", Toast.LENGTH_SHORT).show()
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
        smsButton = findViewById(R.id.btn_sms)
        mobileEditText = findViewById(R.id.edit_company_mobile)
        authCodeEditText = findViewById(R.id.edit_company_authCode)
        loginButton.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            if (mobile.length != 11) {
                mobileEditText.error = "手机号码格式不正确"
                Toast.makeText(applicationContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val authCode = authCodeEditText.text.toString()
            CompanyUserHttp.smsSignin(mobile = mobile, authCode = authCode, handler = companyUserLoginHandler)
        }
        smsButton.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            if (mobile.length != 11) {
                mobileEditText.error = "手机号码格式不正确"
                Toast.makeText(applicationContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SmsHttp.sms(mobile, smsHandler);
        }
    }
}
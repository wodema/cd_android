package com.dzq.coursedesign_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dzq.coursedesign_android.company.CompanyMainActivity
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyUserHttp
import com.dzq.coursedesign_android.http.SmsHttp
import com.dzq.coursedesign_android.http.StudentHttp
import com.dzq.coursedesign_android.student.StudentMainActivity
import com.dzq.coursedesign_android.utils.GsonUtil

class CompanyLoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var smsButton: Button
    private lateinit var mobileEditText: EditText
    private lateinit var authCodeEditText: EditText
    private lateinit var companyRadioButton: RadioButton
    private lateinit var studentRadioButton: RadioButton

    private val companyUserLoginHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录成功:${result}", Toast.LENGTH_SHORT).show()
                    val sp = getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    sp.edit().apply {
                        putString("data", GsonUtil.objToJson(result.data))
                        apply()
                    }
                    Intent(applicationContext, CompanyMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(this)
                    }
                }
                else -> {
                    Log.e("asaa", msg.toString())
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录异常:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val studentLoginHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "登录成功:${result}", Toast.LENGTH_SHORT).show()
                    val sp = getSharedPreferences("student_user", Context.MODE_PRIVATE)
                    sp.edit().apply {
                        putString("data", GsonUtil.objToJson(result.data))
                        apply()
                    }
                    Intent(applicationContext, StudentMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(this)
                    }
                }
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
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        loginButton = findViewById(R.id.btn_login)
        smsButton = findViewById(R.id.btn_sms)
        mobileEditText = findViewById(R.id.edit_login_mobile)
        authCodeEditText = findViewById(R.id.edit_login_authCode)
        companyRadioButton = findViewById(R.id.rb_company)
        studentRadioButton = findViewById(R.id.rb_student)
        loginButton.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            if (mobile.length != 11) {
                mobileEditText.error = "手机号码格式不正确"
                Toast.makeText(applicationContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (companyRadioButton.isChecked) {
                val authCode = authCodeEditText.text.toString()
                CompanyUserHttp.smsSignin(mobile = mobile, authCode = authCode, handler = companyUserLoginHandler)
            } else {
                val authCode = authCodeEditText.text.toString()
                StudentHttp.smsSignin(mobile = mobile, authCode = authCode, handler = studentLoginHandler)
            }
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
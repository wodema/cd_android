package com.dzq.coursedesign_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.dzq.coursedesign_android.company.CompanyMainActivity
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyUserHttp
import com.dzq.coursedesign_android.http.SmsHttp
import com.dzq.coursedesign_android.http.StudentHttp
import com.dzq.coursedesign_android.student.StudentMainActivity
import com.dzq.coursedesign_android.utils.CountDownTimerUtil
import com.dzq.coursedesign_android.utils.GsonUtil
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: AppCompatButton
    private lateinit var smsTextView: TextView
    private lateinit var mobileEditText: EditText
    private lateinit var authCodeEditText: EditText
    private lateinit var changeLoginTypeButton: AppCompatButton
    private lateinit var loginIconImageView: ImageFilterView
    private lateinit var loginInfoTextView: TextView
    private var companyLogin = true

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
        smsTextView = findViewById(R.id.text_sms)
        mobileEditText = findViewById(R.id.edit_login_mobile)
        authCodeEditText = findViewById(R.id.edit_login_authCode)
        changeLoginTypeButton = findViewById(R.id.btn_change_login_type)
        loginIconImageView = findViewById(R.id.img_login_icon)
        loginInfoTextView = findViewById(R.id.text_login_info)

        loginButton.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            if (mobile.length != 11) {
                mobileEditText.error = "手机号码格式不正确"
                Toast.makeText(applicationContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (companyLogin) {
                val authCode = authCodeEditText.text.toString()
                CompanyUserHttp.smsSignin(mobile = mobile, authCode = authCode, handler = companyUserLoginHandler)
            } else {
                val authCode = authCodeEditText.text.toString()
                StudentHttp.smsSignin(mobile = mobile, authCode = authCode, handler = studentLoginHandler)
            }
        }
        smsTextView.setOnClickListener {
            val mobile = mobileEditText.text.toString()
            if (mobile.length != 11) {
                Toast.makeText(applicationContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SmsHttp.sms(mobile, smsHandler)
            CountDownTimerUtil(smsTextView, 60000, 1000).start()
        }

        changeLoginTypeButton.setOnClickListener {
            if (companyLogin) {
                loginInfoTextView.text = "学生登录"
                loginIconImageView.setImageResource(R.drawable.student_login)
                companyLogin = false
            } else {
                loginInfoTextView.text = "单位登录"
                loginIconImageView.setImageResource(R.drawable.company_login)
                companyLogin = true
            }
        }
    }
}
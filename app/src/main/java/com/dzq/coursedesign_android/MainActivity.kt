package com.dzq.coursedesign_android

import android.content.Context
import android.os.Bundle
import android.os.Looper
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
            Thread {
                try {
                    val mobile = mobileEditText.text.toString()
                    val authCode = authCodeEditText.text.toString()
                    val response = CompanyUserHttp.smsSignin(mobile = mobile, authCode = authCode)
                    val responseData = response.body!!.string()
                    val result = gson.fromJson(responseData, Result::class.java)
                    if (result.code == -1) {
                        Looper.prepare()
                        Toast.makeText(this, "登录异常:${result.message}", Toast.LENGTH_SHORT).show()
                        Looper.loop()
                    } else if (result.code == 200) {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                    Looper.prepare()
                    Toast.makeText(this, "登录异常:${e.message}", Toast.LENGTH_SHORT).show()
                    Looper.loop()
                }
            }.start()
        }
    }
}
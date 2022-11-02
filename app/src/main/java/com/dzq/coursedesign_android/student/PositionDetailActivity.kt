package com.dzq.coursedesign_android.student

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.adpter.CompanyPositionAdapter
import com.dzq.coursedesign_android.databinding.ActivityPostionDetailsBinding
import com.dzq.coursedesign_android.databinding.ActivityStudentMainBinding
import com.dzq.coursedesign_android.entity.*
import com.dzq.coursedesign_android.http.CompanyInfoHttp
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.http.ResumeHttp
import com.dzq.coursedesign_android.utils.GsonUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class PositionDetailActivity : AppCompatActivity() {

    private var companyPosition: CompanyPosition? = null

    private lateinit var companyInfo: CompanyInfo

    private val positionInfoHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    companyPosition = GsonUtil.fromJson(GsonUtil.objToJson(result.data))
                    getCompanyInfo()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "获取职位信息失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val companyInfoHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    companyInfo = GsonUtil.fromJson(
                        GsonUtil.objToJson(result.data))
                    initView(binding.root)
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "获取公司信息失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val positionApplyHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "投递成功", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    val result = msg.obj as Result
                    if (result.message == "已经投递过该职位了") {
                        Toast.makeText(applicationContext, "已经投递过该职位了", Toast.LENGTH_SHORT).show()
                        return
                    }
                    Toast.makeText(applicationContext, "投递失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private lateinit var binding: ActivityPostionDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        companyPosition = intent.getParcelableExtra("companyPosition")
        if (companyPosition == null) {
            intent.getIntExtra("positionId", -1).apply {
                CompanyPositionHttp.getById(this, positionInfoHandler)
            }
        }
    }

    private fun getCompanyInfo() {
        CompanyInfoHttp.get(companyPosition!!.companyId!!, companyInfoHandler)
    }

    private fun initView(root: ConstraintLayout) {
        root.apply {
            findViewById<TextView>(R.id.detail_position_name).text = companyInfo.companyName
            findViewById<TextView>(R.id.detail_company_address).text = companyInfo.companyAddress
            findViewById<TextView>(R.id.detail_company_email).text = companyInfo.companyEmail
            findViewById<TextView>(R.id.detail_company_mobile).text = companyInfo.companyMobile
            findViewById<TextView>(R.id.detail_company_introduction).text = companyInfo.companyIntroduction
            companyInfo.companyLogo?.let { Glide.with(this.context).load(it).into(findViewById<ImageFilterView>(R.id.detail_company_logo)) }

            findViewById<TextView>(R.id.detail_position_name).text = companyPosition?.positionName
            findViewById<TextView>(R.id.detail_position_education).text = companyPosition?.education
            findViewById<TextView>(R.id.detail_position_major).text = companyPosition?.positionMajor
            findViewById<TextView>(R.id.detail_position_number).text = companyPosition?.positionNumber
            findViewById<TextView>(R.id.detail_position_requirement).text = companyPosition?.positionRequirement
            findViewById<TextView>(R.id.detail_position_salary).text = companyPosition?.positionSalary
            findViewById<TextView>(R.id.detail_position_responsibility).text = companyPosition?.positionResponsibility

            findViewById<ImageView>(R.id.detail_img_back).setOnClickListener {
                onBackPressed()
            }

            findViewById<AppCompatButton>(R.id.btn_company_position_apply).setOnClickListener {
                context.getSharedPreferences("student_user", Context.MODE_PRIVATE).getString("data", "").apply {
                    val student = GsonUtil.fromJson<Student>(this)
                    ResumeHttp.applyPosition(companyPosition!!, student, positionApplyHandler)
                }

            }
        }
    }
}
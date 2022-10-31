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
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class CompanyPositionSaveActivity : AppCompatActivity() {

    private var companyPosition = CompanyPosition()
    private lateinit var positionNameEditText: EditText
    private lateinit var positionNumberEditText: EditText
    private lateinit var positionSalaryEditText: EditText
    private lateinit var positionRequirementEditText: EditText
    private lateinit var positionWorkCityEditText: EditText
    private lateinit var positionEducationEditText: EditText
    private lateinit var positionMajorEditText: EditText
    private lateinit var positionResponsibilityEditText: EditText
    private lateinit var positionSaveButton: Button

    private val companyPositionSaveHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                    Intent(applicationContext, CompanyMainActivity::class.java).apply {
                        startActivity(this)
                    }
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "保存失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_position_save)
        initView()
        intent.getParcelableExtra<CompanyPosition?>("companyPosition")?.apply {
            positionNameEditText.setText(this.companyName)
            positionNumberEditText.setText(this.positionNumber)
            positionMajorEditText.setText(this.positionMajor)
            positionSalaryEditText.setText(this.positionSalary)
            positionWorkCityEditText.setText(this.workCity)
            positionEducationEditText.setText(this.education)
            positionRequirementEditText.setText(this.positionRequirement)
            positionResponsibilityEditText.setText(this.positionResponsibility)
            companyPosition = this
        }
        positionSaveButton.setOnClickListener {
            CompanyPositionHttp.save(companyPosition.apply {
                positionName = positionNameEditText.text.toString()
                positionNumber = positionNumberEditText.text.toString()
                positionMajor = positionMajorEditText.text.toString()
                positionSalary = positionSalaryEditText.text.toString()
                workCity = positionWorkCityEditText.text.toString()
                education = positionEducationEditText.text.toString()
                positionResponsibility = positionResponsibilityEditText.text.toString()
                getSharedPreferences("company_user", Context.MODE_PRIVATE).apply {
                    getString("data", null)!!.apply {
                        val companyUser = GsonUtil.fromJson<CompanyUser>(this)
                        companyId = companyUser.companyId
                    }
                }
                positionRequirement = positionRequirementEditText.text.toString()
            }, companyPositionSaveHandler)
        }
    }

    private fun initView() {
        positionNameEditText = findViewById(R.id.company_position_name)
        positionMajorEditText = findViewById(R.id.company_position_major)
        positionSalaryEditText = findViewById(R.id.company_position_salary)
        positionRequirementEditText = findViewById(R.id.company_position_requirement)
        positionResponsibilityEditText = findViewById(R.id.company_position_responsibility)
        positionWorkCityEditText = findViewById(R.id.company_position_city)
        positionEducationEditText = findViewById(R.id.company_position_education)
        positionNumberEditText = findViewById(R.id.company_position_number)
        positionSaveButton = findViewById(R.id.btn_company_position_save)
    }
}
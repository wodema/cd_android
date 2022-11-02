package com.dzq.coursedesign_android.company

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.databinding.ActivityStudentDetailsBinding
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.entity.ResumeRecords
import com.dzq.coursedesign_android.entity.Student
import com.dzq.coursedesign_android.http.StudentHttp
import com.dzq.coursedesign_android.student.StudentResumeActivity
import com.dzq.coursedesign_android.utils.GsonUtil

class StudentDetailsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityStudentDetailsBinding

    private lateinit var resumeRecord: ResumeRecords

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resumeRecord = intent.getParcelableExtra("resumeRecord")!!

        initView(binding.root)

    }

    private fun initView(root: ConstraintLayout) {
        root.apply {
            resumeRecord.studentAvatar?.let {
                Glide.with(this.context).load(it).into(findViewById<ImageFilterView>(R.id.img_resume_student_avatar))
            }
            findViewById<TextView>(R.id.text_resume_student_name).text = resumeRecord.studentName
            findViewById<TextView>(R.id.text_resume_student_mobile).text = resumeRecord.studentMobile
            findViewById<TextView>(R.id.text_resume_student_email).text = resumeRecord.studentEmail
            findViewById<TextView>(R.id.text_resume_student_school).text = resumeRecord.studentSchool
            findViewById<TextView>(R.id.text_resume_student_major).text = resumeRecord.studentMajor
            findViewById<TextView>(R.id.text_resume_student_graduation_date).text = resumeRecord.studentGraduationDate.toString()
            findViewById<TextView>(R.id.btn_resume_student_preview).setOnClickListener {
                Intent(applicationContext, StudentResumeActivity::class.java).apply {
                    putExtra("resumeUrl", resumeRecord.resumeUrl)
                    startActivity(this)
                }
            }
        }
    }

}
package com.dzq.coursedesign_android.student

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.databinding.ActivityResumePreviewBinding
import com.zjy.pdfview.PdfView

class StudentResumeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityResumePreviewBinding
    private lateinit var studentResumePdfView: PdfView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding.root)

        val resumeUrl = intent.getStringExtra("resumeUrl")
        studentResumePdfView.loadPdf(resumeUrl)
    }

    private fun initView(root: ConstraintLayout) {
        studentResumePdfView = root.findViewById(R.id.pdf_student_resume)
    }
}
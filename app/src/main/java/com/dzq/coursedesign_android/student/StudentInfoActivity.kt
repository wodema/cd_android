package com.dzq.coursedesign_android.student

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.databinding.ActivityPostionDetailsBinding
import com.dzq.coursedesign_android.databinding.ActivityStudentInfoBinding
import com.dzq.coursedesign_android.entity.CompanyInfo
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.entity.Student
import com.dzq.coursedesign_android.http.CompanyInfoHttp
import com.dzq.coursedesign_android.http.OssHttp
import com.dzq.coursedesign_android.http.StudentHttp
import com.dzq.coursedesign_android.utils.GsonUtil
import com.zjy.pdfview.PdfView
import java.io.File
import java.time.LocalDate
import java.util.*

class StudentInfoActivity : AppCompatActivity(){

    private lateinit var binding: ActivityStudentInfoBinding
    private lateinit var studentAvatarImageFilterView: ImageFilterView
    private lateinit var studentNameEditText: EditText
    private lateinit var studentMobileEditText: EditText
    private lateinit var studentEmailEditText: EditText
    private lateinit var studentSchoolEditText: EditText
    private lateinit var studentMajorEditText: EditText
    private lateinit var studentGraduationDateText: TextView
    private lateinit var studentInfoSaveButton: AppCompatButton
    private lateinit var studentResumeUploadButton: AppCompatButton
    private lateinit var studentResumePreviewButton: AppCompatButton
    private val calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private var student = Student()
    private val IMAGE_REQUEST_CODE = 1
    private val PDF_REQUEST_CODE = 2


    private val resumeUploadHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    student.resumeUrl = result.data as String
                    Toast.makeText(applicationContext, "上传成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "上传失败:${result.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val fileUploadHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    student.studentAvatar = result.data as String
                    Glide.with(applicationContext)
                        .load(student.studentAvatar)
                        .into(studentAvatarImageFilterView)
                    Toast.makeText(applicationContext, "上传成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "上传失败:${result.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private val studentSaveHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val sp = applicationContext?.getSharedPreferences("student_user", Context.MODE_PRIVATE)
                    student = GsonUtil.fromJson(GsonUtil.objToJson(result.data))
                    sp?.edit()?.apply {
                        putString("data", GsonUtil.objToJson(student))
                        apply()
                    }
                    Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "保存失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding.root)

        application.getSharedPreferences("student_user", Context.MODE_PRIVATE)?.getString("data", null)?.let {
            student = GsonUtil.fromJson(it)
            student.apply {
                studentNameEditText.setText(studentName)
                studentMobileEditText.setText(studentMobile)
                studentEmailEditText.setText(studentEmail)
                studentSchoolEditText.setText(school)
                studentMajorEditText.setText(major)
                studentGraduationDateText.text = graduationDate?.toString()
                studentAvatar?.let { it1 -> Glide.with(applicationContext).load(it1).into(studentAvatarImageFilterView)}
            }
        }

    }

    @SuppressLint("SetTextI18n", "NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView(root: ConstraintLayout) {
        root.apply {
            studentAvatarImageFilterView = findViewById(R.id.img_student_avatar)
            studentNameEditText = findViewById(R.id.edit_student_name)
            studentMobileEditText = findViewById(R.id.edit_student_mobile)
            studentEmailEditText = findViewById(R.id.edit_student_email)
            studentSchoolEditText = findViewById(R.id.edit_student_school)
            studentMajorEditText = findViewById(R.id.edit_student_major)
            studentGraduationDateText = findViewById(R.id.text_student_graduationDate)
            studentInfoSaveButton = findViewById(R.id.btn_student_info_save)
            studentResumePreviewButton = findViewById(R.id.btn_student_preview_resume)
            studentResumeUploadButton = findViewById(R.id.btn_student_upload_resume)
            datePickerDialog = DatePickerDialog(this.context,
                { _, p1, p2, p3 ->
                    studentGraduationDateText.text = "$p1-${if (p2 < 10) "0$p2" else "$p2"}-${if (p3 < 10) "0$p3" else "$p3"}"
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            studentGraduationDateText.setOnClickListener {
                if(!datePickerDialog.isShowing) {
                    datePickerDialog.show()
                }
            }

            studentResumeUploadButton.setOnClickListener {
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    startActivityForResult(this, PDF_REQUEST_CODE)
                }
            }

            studentResumePreviewButton.setOnClickListener {
                if (student.resumeUrl == null) {
                    Toast.makeText(applicationContext, "还未上传简历", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Intent(applicationContext, StudentResumeActivity::class.java).apply {
                    putExtra("resumeUrl", student.resumeUrl)
                    startActivity(this)
                }
            }

            studentAvatarImageFilterView.setOnClickListener {
                Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                    startActivityForResult(this, IMAGE_REQUEST_CODE)
                }
            }

            studentInfoSaveButton.setOnClickListener {
                StudentHttp.save(student.apply {
                    studentName = studentNameEditText.text.toString()
                    studentMobile = studentMobileEditText.text.toString()
                    studentEmail = studentEmailEditText.text.toString()
                    school = studentSchoolEditText.text.toString()
                    major = studentMajorEditText.text.toString()
                    graduationDate = LocalDate.parse(studentGraduationDateText.text)
                }, studentSaveHandler)
            }
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101);
        }
        if (requestCode == IMAGE_REQUEST_CODE) {
            //相册
            val fileUri = data!!.data
            //获取照片路径
            val filePathColumn = arrayOf(MediaStore.Audio.Media.DATA)
            val cursor = contentResolver.query(fileUri!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close()
            OssHttp.upload(filePath, fileUploadHandler)
        } else if (requestCode == PDF_REQUEST_CODE){
            val fileUri = data!!.data
            val filePath =  "/storage/emulated/0/" + fileUri!!.path!!.split(":")[1]
            //获取pdf路径
            OssHttp.upload(filePath, resumeUploadHandler)
        }
    }
}
package com.dzq.coursedesign_android.company

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.entity.CompanyInfo
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyInfoHttp
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.http.OssHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class CompanyInfoSaveActivity: AppCompatActivity() {

    private var companyInfo = CompanyInfo()
    private lateinit var companyNameEditText: EditText
    private lateinit var companyAddressEditText: EditText
    private lateinit var companyMobileEditText: EditText
    private lateinit var companyWebsiteEditText: EditText
    private lateinit var companyIntroductionEditText: EditText
    private lateinit var companyEmailEditText: EditText
    private lateinit var companyLogoImageFilterView: ImageFilterView
    private lateinit var companyInfoSaveButton: Button

    private val IMAGE_REQUEST_CODE = 1

    private val fileUploadHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    companyInfo.companyLogo = result.data as String
                    Glide.with(this@CompanyInfoSaveActivity)
                        .load(companyInfo.companyLogo)
                        .into(companyLogoImageFilterView)
                    Toast.makeText(applicationContext, "上传成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(applicationContext, "上传失败:${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val companyInfoSaveHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val sp = applicationContext?.getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    val companyUser = GsonUtil.fromJson<CompanyUser>(sp?.getString("data", ""))
                    companyUser.companyInfo = GsonUtil.fromJson(GsonUtil.objToJson(result.data))
                    sp?.edit()?.apply {
                        putString("data", GsonUtil.objToJson(companyUser))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_info_save)
        initView()
        intent.getParcelableExtra<CompanyInfo?>("companyInfo")?.apply {
            companyNameEditText.setText(this.companyName)
            companyAddressEditText.setText(this.companyAddress)
            companyMobileEditText.setText(this.companyMobile)
            companyWebsiteEditText.setText(this.companyWebsite)
            companyIntroductionEditText.setText(this.companyIntroduction)
            companyEmailEditText.setText(this.companyEmail)
            if (this.companyLogo != null) {
                Glide.with(this@CompanyInfoSaveActivity)
                    .load(this.companyLogo)
                    .into(companyLogoImageFilterView)
            }
            companyInfo = this
        }
        companyInfoSaveButton.setOnClickListener {
            CompanyInfoHttp.save(companyInfo.apply {
                companyName = companyNameEditText.text.toString()
                companyAddress = companyAddressEditText.text.toString()
                companyMobile = companyMobileEditText.text.toString()
                companyWebsite = companyWebsiteEditText.text.toString()
                companyIntroduction = companyIntroductionEditText.text.toString()
                companyEmail = companyEmailEditText.text.toString()
                if (id == null) {
                    getSharedPreferences("company_user", Context.MODE_PRIVATE).apply {
                        getString("data", null)!!.apply {
                            val companyUser = GsonUtil.fromJson<CompanyUser>(this)
                            id = companyUser.companyInfo!!.id
                        }
                    }
                }
            }, companyInfoSaveHandler)
        }

        companyLogoImageFilterView.setOnClickListener {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                startActivityForResult(this, IMAGE_REQUEST_CODE)
            }
            Toast.makeText(applicationContext, "点击了图片", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        companyNameEditText = findViewById(R.id.edit_company_name)
        companyAddressEditText = findViewById(R.id.edit_company_address)
        companyMobileEditText = findViewById(R.id.edit_company_mobile)
        companyWebsiteEditText = findViewById(R.id.edit_company_website)
        companyIntroductionEditText = findViewById(R.id.edit_company_introduction)
        companyEmailEditText = findViewById(R.id.edit_company_email)
        companyLogoImageFilterView = findViewById(R.id.img_company_logo)
        companyInfoSaveButton = findViewById(R.id.btn_company_info_save)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE) {//相册
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101);
            }
            val imageUri = data!!.data
            //获取照片路径
            val filePathColumn = arrayOf(MediaStore.Audio.Media.DATA)
            val cursor = contentResolver.query(imageUri!!, filePathColumn, null, null, null);
            cursor!!.moveToFirst();
            val photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            Log.d("photoPath", photoPath)
            OssHttp.upload(photoPath, fileUploadHandler)
        }
    }
}
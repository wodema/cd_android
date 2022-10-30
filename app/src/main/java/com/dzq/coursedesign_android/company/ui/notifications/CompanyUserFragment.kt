package com.dzq.coursedesign_android.company.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.company.CompanyInfoSaveActivity
import com.dzq.coursedesign_android.CompanyLoginActivity
import com.dzq.coursedesign_android.databinding.FragmentCompanyUserBinding
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyUserHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class CompanyUserFragment : Fragment() {

    private var _binding: FragmentCompanyUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val bindMobileHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val sp = context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    sp?.edit()?.apply {
                        putString("data", GsonUtil.objToJson(result.data))
                        apply()
                    }
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(context, "修改失败:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val authenticationHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val sp = context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    sp?.edit()?.apply {
                        putString("data", GsonUtil.objToJson(result.data))
                        apply()
                    }
                    Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(context, "认证失败:${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView(root)

        return root
    }

    private fun initView(root: View) {
        // 注销
        root.findViewById<AppCompatButton>(R.id.btn_logout).setOnClickListener {
            context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)?.edit()?.apply {
                clear()
                apply()
            }
            Intent(context, CompanyLoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }

        // 更绑手机号
        root.findViewById<AppCompatButton>(R.id.btn_user_mobile_bind).setOnClickListener {
            context?.let { it1 ->
                val editText = EditText(it1)
                editText.hint = "请输入新的手机号"
                AlertDialog.Builder(it1)
                .setTitle("更绑手机号")
                .setView(editText)
                .setPositiveButton("确认") { _, _ ->
                    if (editText.text == null || editText.text.length != 11) {
                        Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                    } else {
                        context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)?.apply {
                            val companyUser = GsonUtil.fromJson<CompanyUser>(getString("data", null))
                            CompanyUserHttp.bindMobile(companyUser.id!!, editText.text.toString(), bindMobileHandler)
                        }
                    }
                }
                .create()
                .show()
            }
        }

        // 身份认证
        root.findViewById<AppCompatButton>(R.id.btn_company_user_auth).setOnClickListener {
            context?.let { it1 ->
                val factory = LayoutInflater.from(it1)
                val linearLayout = factory.inflate(R.layout.company_user_authentication, null)
                val companyUser = GsonUtil.fromJson<CompanyUser>(it1
                    .getSharedPreferences("company_user", Context.MODE_PRIVATE)
                    ?.getString("data", null))
                val idCardEditText = linearLayout.findViewById<EditText>(R.id.edit_authentication_id_card)
                val usernameEditText = linearLayout.findViewById<EditText>(R.id.edit_authentication_username)
                val emailEditText = linearLayout.findViewById<EditText>(R.id.edit_authentication_email)
                companyUser.idCard?.let { it2 -> idCardEditText.setText(it2) }
                companyUser.userName?.let { it2 -> usernameEditText.setText(it2) }
                companyUser.userEmail?.let { it2 -> emailEditText.setText(it2) }

                AlertDialog.Builder(it1)
                    .setTitle("身份认证")
                    .setView(linearLayout)
                    .setPositiveButton("确认") { _, _ ->
                        if (idCardEditText.text == null || idCardEditText.text.length != 18 || usernameEditText.text == null) {
                            Toast.makeText(context, "身份证号或姓名格式错误", Toast.LENGTH_SHORT).show()
                        } else {
                            context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)?.apply {
                                companyUser.idCard = idCardEditText.text.toString()
                                companyUser.userEmail = emailEditText.text.toString()
                                companyUser.userName = usernameEditText.text.toString()
                                CompanyUserHttp.authentication(companyUser, authenticationHandler)
                            }
                        }
                    }
                    .create()
                    .show()
            }
        }

        // 公司信息
        root.findViewById<AppCompatButton>(R.id.btn_company_info).setOnClickListener {
            context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)?.apply {
                val companyUser = GsonUtil.fromJson<CompanyUser>(getString("data", null))
                Intent(context, CompanyInfoSaveActivity::class.java).apply {
                    putExtra("companyInfo", companyUser.companyInfo)
                    startActivity(this)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
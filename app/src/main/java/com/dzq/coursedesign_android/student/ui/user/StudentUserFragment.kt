package com.dzq.coursedesign_android.student.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.LoginActivity
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.company.CompanyMainActivity
import com.dzq.coursedesign_android.databinding.FragmentStudentUserBinding
import com.dzq.coursedesign_android.entity.Student
import com.dzq.coursedesign_android.student.StudentInfoActivity
import com.dzq.coursedesign_android.utils.GsonUtil

class StudentUserFragment : Fragment() {

    private var _binding: FragmentStudentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView(root)

        return root
    }

    private fun initView(root: View) {
        root.context.getSharedPreferences("student_user", Context.MODE_PRIVATE)?.getString("data", "")?.let {
            val student = GsonUtil.fromJson<Student>(it)
            student.studentName?.let { it1 -> root.findViewById<TextView>(R.id.text_student_name).text = it1}
            student.studentMobile?.let { it1 -> root.findViewById<TextView>(R.id.text_student_mobile).text = it1}
            student.studentEmail?.let { it1 -> root.findViewById<TextView>(R.id.text_student_email).text = it1}
            student.studentAvatar?.let {it1 -> Glide.with(root.context).load(it1).into(root.findViewById(R.id.card_student_avatar))}
        }
        // 信息
        root.findViewById<AppCompatButton>(R.id.btn_student_info).setOnClickListener{
            Intent(root.context, StudentInfoActivity::class.java).apply {
                startActivity(this)
            }
        }


        // 作者
        root.findViewById<AppCompatButton>(R.id.btn_developer).setOnClickListener {
            context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setTitle("作者")
                    .setPositiveButton("确认") { _, _ -> }
                    .setMessage("3119005136戴梓庆")
                    .create()
                    .show()
            }
        }

        // 切换到公司端
        root.findViewById<AppCompatButton>(R.id.btn_change_to_company).setOnClickListener {
            val companyJson = context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)
                ?.getString("data", null)
            if (companyJson == null) {
                Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(this)
                }
            } else {
                Intent(context, CompanyMainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(this)
                }
            }
        }


        root.findViewById<AppCompatButton>(R.id.btn_student_logout).setOnClickListener {
            context?.getSharedPreferences("student_user", Context.MODE_PRIVATE)?.edit()?.apply {
                clear()
                apply()
            }
            Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
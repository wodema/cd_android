package com.dzq.coursedesign_android.student.ui.resume

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzq.coursedesign_android.adpter.CompanyResumeAdapter
import com.dzq.coursedesign_android.adpter.StudentResumeAdapter
import com.dzq.coursedesign_android.databinding.FragmentStudentResumeBinding
import com.dzq.coursedesign_android.databinding.FragmentStudentUserBinding
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.entity.ResumeRecords
import com.dzq.coursedesign_android.entity.Student
import com.dzq.coursedesign_android.http.ResumeHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class StudentResumeFragment: Fragment() {

    private var _binding: FragmentStudentResumeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val studentResumeHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val resumeRecordList = GsonUtil.fromJson<MutableList<ResumeRecords>>(GsonUtil.objToJson(result.data))
                    val recyclerView = binding.rvStudentResume
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = StudentResumeAdapter(resumeRecordList)
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(context, "获取数据异常:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentResumeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getResumeByStudentId()

        return root
    }

    private fun getResumeByStudentId() {
        context?.getSharedPreferences("student_user", Context.MODE_PRIVATE)?.apply {
            val student = GsonUtil.fromJson<Student>(getString("data", ""))
            if (student.id == null) {
                Toast.makeText(context, "暂未登录", Toast.LENGTH_SHORT).show()
                return
            }
            ResumeHttp.getStudentResumeList(student.id!!, studentResumeHandler)
        }

    }
}
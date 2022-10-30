package com.dzq.coursedesign_android.student.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.adpter.CompanyPositionAdapter
import com.dzq.coursedesign_android.adpter.StudentPositionAdapter
import com.dzq.coursedesign_android.company.CompanyPositionSaveActivity
import com.dzq.coursedesign_android.databinding.FragmentStudentPositionBinding
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.CompanyUser
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class HomeFragment : Fragment() {

    private var _binding: FragmentStudentPositionBinding? = null

    private val studentPositionHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val studentPositionList = GsonUtil.fromJson<MutableList<CompanyPosition>>(
                        GsonUtil.objToJson(result.data))
                    val recyclerView = binding.rvStudentPosition
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = StudentPositionAdapter(studentPositionList)
                }
                else -> {
                    val result = msg.obj as Result
                    Toast.makeText(context, "获取数据异常:${result.message }", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStudentPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getPositions()

        return root
    }

    private fun getPositions() {
        CompanyPositionHttp.getCompanyPositionList(studentPositionHandler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
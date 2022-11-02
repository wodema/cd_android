package com.dzq.coursedesign_android.student.ui.position

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.adpter.StudentPositionAdapter
import com.dzq.coursedesign_android.databinding.FragmentStudentPositionBinding
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class StudentPositionFragment : Fragment() {

    private var _binding: FragmentStudentPositionBinding? = null

    private lateinit var positionEditText: EditText

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
                    Toast.makeText(context, "获取数据异常:${result.message}", Toast.LENGTH_SHORT).show()
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

        initView(root)

        getPositions()

        return root
    }

    private fun initView(root: View) {
        positionEditText = root.findViewById(R.id.edit_position_search)

        root.findViewById<TextView>(R.id.text_position_search).setOnClickListener {
            CompanyPositionHttp.getCompanyPositionList(positionEditText.text.toString(), studentPositionHandler)
        }

        root.findViewById<LinearLayout>(R.id.ll_position_reset).setOnClickListener {
            positionEditText.text = null
            CompanyPositionHttp.getCompanyPositionList(null, studentPositionHandler)
        }
    }

    private fun getPositions() {
        CompanyPositionHttp.getCompanyPositionList(null, studentPositionHandler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
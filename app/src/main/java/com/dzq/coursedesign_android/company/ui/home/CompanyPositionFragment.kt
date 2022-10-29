package com.dzq.coursedesign_android.company.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.adpter.CompanyPositionAdapter
import com.dzq.coursedesign_android.company.CompanyPositionSaveActivity
import com.dzq.coursedesign_android.databinding.FragmentCompanyPositionBinding
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.utils.GsonUtil

class CompanyPositionFragment : Fragment() {

    private var _binding: FragmentCompanyPositionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val companyPositionHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                200 -> {
                    val result = msg.obj as Result
                    val companyPositionList = GsonUtil.fromJson<MutableList<CompanyPosition>>(GsonUtil.objToJson(result.data))
                    val recyclerView = binding.rvCompanyPosition
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = CompanyPositionAdapter(companyPositionList)
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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentCompanyPositionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView(root)
        getPositionsByCompanyId()

        return root
    }

    private fun initView(root: View) {
        root.findViewById<AppCompatButton>(R.id.btn_company_position_add).setOnClickListener {
            Intent(context, CompanyPositionSaveActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getPositionsByCompanyId() {
        val companyId = context?.getSharedPreferences("company_user", Context.MODE_PRIVATE)?.getInt("companyId", -1)
        if (companyId == -1) {
            Toast.makeText(context, "暂未登录", Toast.LENGTH_SHORT).show()
            return
        }
        CompanyPositionHttp.getCompanyPositionList(companyId, companyPositionHandler)
    }
}
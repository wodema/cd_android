package com.dzq.coursedesign_android.adpter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.company.CompanyPositionSaveActivity
import com.dzq.coursedesign_android.company.StudentDetailsActivity
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.entity.ResumeRecords
import com.dzq.coursedesign_android.http.CompanyPositionHttp

class CompanyResumeAdapter(
    private val itemInfoList: MutableList<ResumeRecords>,
) :  RecyclerView.Adapter<CompanyResumeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyResumeViewHolder {
        return CompanyResumeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_resume, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CompanyResumeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_resume_student_name).text = itemInfoList[position].studentName
            findViewById<TextView>(R.id.item_resume_student_school).text = itemInfoList[position].studentSchool
            findViewById<TextView>(R.id.item_resume_student_major).text = itemInfoList[position].studentMajor
            findViewById<TextView>(R.id.item_resume_position_education).text = itemInfoList[position].educationRequired
            findViewById<TextView>(R.id.item_resume_position_salary).text = itemInfoList[position].positionSalary
            findViewById<TextView>(R.id.item_resume_company_position).text = itemInfoList[position].positionName
            findViewById<TextView>(R.id.item_resume_company_apply_date).text = itemInfoList[position].applyDate.toString().replace("T", " ")
            itemInfoList[position].studentAvatar?.let {
                Glide.with(this.context).load(it).into(findViewById(R.id.item_resume_student_avatar))
            }
            findViewById<LinearLayout>(R.id.ll_company_resume_info).setOnClickListener {
                Intent(context, StudentDetailsActivity::class.java).apply {
                    putExtra("resumeRecord", itemInfoList[position])
                    context.startActivity(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return itemInfoList.size
    }

}


class CompanyResumeViewHolder(view: View): RecyclerView.ViewHolder(view)
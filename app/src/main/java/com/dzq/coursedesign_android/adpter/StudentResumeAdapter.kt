package com.dzq.coursedesign_android.adpter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.company.StudentDetailsActivity
import com.dzq.coursedesign_android.entity.ResumeRecords
import com.dzq.coursedesign_android.student.PositionDetailActivity

class StudentResumeAdapter(
    private val itemInfoList: MutableList<ResumeRecords>,
) :  RecyclerView.Adapter<StudentResumeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentResumeViewHolder {
        return StudentResumeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_student_resume, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: StudentResumeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_resume_student_company_name).text = itemInfoList[position].companyName
            findViewById<TextView>(R.id.item_resume_student_position).text = itemInfoList[position].positionName
            findViewById<TextView>(R.id.item_resume_student_position_salary).text = itemInfoList[position].positionSalary
            findViewById<TextView>(R.id.item_resume_work_city).text = itemInfoList[position].workCity
            findViewById<TextView>(R.id.item_resume_student_education).text = itemInfoList[position].educationRequired
            findViewById<TextView>(R.id.item_resume_student_apply_date).text = itemInfoList[position].applyDate.toString().replace("T", " ")
            itemInfoList[position].studentAvatar?.let {
                Glide.with(this.context).load(it).into(findViewById(R.id.item_resume_company_logo))
            }
            findViewById<LinearLayout>(R.id.ll_company_resume_info).setOnClickListener {
                Intent(context, PositionDetailActivity::class.java).apply {
                    putExtra("positionId", itemInfoList[position].positionId)
                    context.startActivity(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return itemInfoList.size
    }

}


class StudentResumeViewHolder(view: View): RecyclerView.ViewHolder(view)
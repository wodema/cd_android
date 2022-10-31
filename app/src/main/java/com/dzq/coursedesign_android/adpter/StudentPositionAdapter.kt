package com.dzq.coursedesign_android.adpter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzq.coursedesign_android.R
import com.dzq.coursedesign_android.company.CompanyPositionSaveActivity
import com.dzq.coursedesign_android.entity.CompanyPosition
import com.dzq.coursedesign_android.entity.Result
import com.dzq.coursedesign_android.http.CompanyPositionHttp
import com.dzq.coursedesign_android.student.PositionDetailActivity

class StudentPositionAdapter(
    private val itemInfoList: MutableList<CompanyPosition>,
) :  RecyclerView.Adapter<StudentPositionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentPositionViewHolder {
        return StudentPositionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_student_position, parent, false))
    }

    override fun onBindViewHolder(holder: StudentPositionViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_student_position_name).text = itemInfoList[position].positionName
            findViewById<TextView>(R.id.item_student_position_number).text = itemInfoList[position].positionNumber.toString()
            findViewById<TextView>(R.id.item_student_position_education).text = itemInfoList[position].education
            findViewById<TextView>(R.id.item_student_position_city).text = itemInfoList[position].workCity
            findViewById<TextView>(R.id.item_student_position_salary).text = itemInfoList[position].positionSalary
            findViewById<TextView>(R.id.item_student_position_major).text = itemInfoList[position].positionMajor
            itemInfoList[position].companyLogo?.let {
                Glide.with(this.context).load(it).into(findViewById(R.id.item_company_logo))
            }
            findViewById<TextView>(R.id.ll_student_position_info).setOnClickListener {
                Intent(context, PositionDetailActivity::class.java).apply {
                    putExtra("companyPosition", itemInfoList[position])
                    context.startActivity(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return itemInfoList.size
    }

}

class StudentPositionViewHolder(view: View): RecyclerView.ViewHolder(view)

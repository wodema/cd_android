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

class CompanyPositionAdapter(
    private val itemInfoList: MutableList<CompanyPosition>,
) :  RecyclerView.Adapter<CompanyPositionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyPositionViewHolder {
        return CompanyPositionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company_position, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CompanyPositionViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.item_company_position_name).text = itemInfoList[position].positionName
            findViewById<TextView>(R.id.item_company_position_number).text = itemInfoList[position].positionNumber.toString()
            findViewById<TextView>(R.id.item_company_position_education).text = itemInfoList[position].education
            findViewById<TextView>(R.id.item_company_position_city).text = itemInfoList[position].workCity
            findViewById<TextView>(R.id.item_company_position_salary).text = itemInfoList[position].positionSalary
            findViewById<TextView>(R.id.item_company_position_major).text = itemInfoList[position].positionMajor
            findViewById<AppCompatButton>(R.id.btn_company_position_edit).setOnClickListener{
                Intent(context, CompanyPositionSaveActivity::class.java).apply {
                    putExtra("companyPosition", itemInfoList[position])
                    context.startActivity(this)
                }
            }
            findViewById<AppCompatButton>(R.id.btn_company_position_delete).setOnClickListener{
                CompanyPositionHttp.deletePosition(itemInfoList[position].id!!,
                    object : Handler(Looper.getMainLooper()) {
                        override fun handleMessage(msg: Message) {
                            super.handleMessage(msg)
                            when (msg.what) {
                                200 -> {
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                                    itemInfoList.removeAt(position);
                                    //删除动画
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                }
                                else -> {
                                    val result = msg.obj as Result
                                    Toast.makeText(context, "删除失败:${result.message }", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                })

            }
        }

    }

    override fun getItemCount(): Int {
        return itemInfoList.size
    }

}


class CompanyPositionViewHolder(view: View): RecyclerView.ViewHolder(view)

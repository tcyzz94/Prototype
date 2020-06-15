package com.example.prototype.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prototype.Interface.ISchedule
import com.example.prototype.Model.Student
import com.example.prototype.R
import kotlinx.android.synthetic.main.item_schedule.view.*

class MondayRvAdapter(var aryMonday: ArrayList<Student>) :
    RecyclerView.Adapter<MondayRvAdapter.MondayHolder>() {

    internal lateinit var callback: ISchedule
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MondayHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return MondayHolder(itemView)
    }

    override fun onBindViewHolder(holder: MondayHolder, position: Int) {
        holder.bindItems(aryMonday[position])
        holder.itemView.rl_item_main.setOnClickListener {
            callback.scheduleCallBack(aryMonday[position])
        }

    }

    override fun getItemCount(): Int {
        return aryMonday.size
    }

    fun setCallBackListener(listen: ISchedule) {
        callback = listen
    }

    fun updateList(aLSchedule: ArrayList<Student>) {
        aryMonday = aLSchedule
        notifyDataSetChanged()

    }

    class MondayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(stdnt: Student) {
            itemView.tv_time.text = stdnt.sStartTime
            itemView.tv_name.text = stdnt.sName
//            if (stdnt.sClassType == "R") {
//                itemView.rl_item_main.setBackgroundColor(Color.RED)
//            }
        }
    }


}
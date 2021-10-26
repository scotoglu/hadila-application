package com.scoto.hadila.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoto.hadila.data.model.UserLog
import com.scoto.hadila.databinding.ItemUserLogBinding

class UserLogAdapter : RecyclerView.Adapter<UserLogAdapter.MyViewHolder>() {

    private lateinit var logs: List<UserLog>


    inner class MyViewHolder(val binding: ItemUserLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cvItemLog.setOnClickListener {
                //
            }
        }

        fun bind(userLog: UserLog) {
            binding.log = userLog
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    fun setLogs(logs: List<UserLog>) {
        this.logs = logs
    }
}
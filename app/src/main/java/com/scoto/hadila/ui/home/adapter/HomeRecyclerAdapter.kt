package com.scoto.hadila.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scoto.hadila.R
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.databinding.ItemProblemBinding
import com.scoto.hadila.ui.common.ItemSelectListener

class HomeRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {


    private lateinit var problems: MutableList<Problem>
    private lateinit var listener: ItemSelectListener


    inner class MyViewHolder(val binding: ItemProblemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.container.setOnClickListener {
                listener.getItem(problems[adapterPosition])
            }
        }

        fun bind(problem: Problem) {
            binding.problem = problem
            if (binding.linearLayoutTagGroup.childCount >= 0) {
                binding.linearLayoutTagGroup.removeAllViews()
                problem.category?.forEach { category ->
                    val textView = LayoutInflater.from(context)
                        .inflate(R.layout.tv_item_category, null, false)
                            as TextView
                    textView.text = category
                    binding.linearLayoutTagGroup.addView(textView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(problems[position])
    }

    override fun getItemCount(): Int {
        return problems.size
    }

    fun setProblems(problems: MutableList<Problem>) {
        this.problems = problems
    }

    fun setListener(listener: ItemSelectListener) {
        this.listener = listener
    }
}
package com.scoto.hadila.ui.problem.problem_listing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.databinding.ItemProblemBinding
import com.scoto.hadila.ui.common.ItemSelectListener


class ProblemListingAdapter : RecyclerView.Adapter<ProblemListingAdapter.MyViewHolder>() {

    private lateinit var problems: List<Problem>
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(problems[position])
    }

    override fun getItemCount(): Int {
        return problems.size
    }

    fun setProblems(problems: List<Problem>) {
        this.problems = problems
    }

    fun setListener(listener: ItemSelectListener) {
        this.listener = listener
    }
}
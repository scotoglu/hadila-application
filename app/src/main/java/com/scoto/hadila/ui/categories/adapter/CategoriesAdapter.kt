package com.scoto.hadila.ui.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoto.hadila.databinding.ItemCategoriesBinding
import com.scoto.hadila.ui.common.ItemSelectListener


class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {


    private lateinit var categories: MutableList<String>
    private lateinit var listener: ItemSelectListener

    inner class MyViewHolder(val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.linearLayoutCategoriesItem.setOnClickListener {
                //implement interface
                listener.getItem(categories[adapterPosition])
            }
        }

        fun bind(item: String) {
            binding.textviewCategoryTitle.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun setCategories(categories: MutableList<String>) {
        this.categories = categories
    }
    fun setListener(listener: ItemSelectListener){
        this.listener = listener
    }
}
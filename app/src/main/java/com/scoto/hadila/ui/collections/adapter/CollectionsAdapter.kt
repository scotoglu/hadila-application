package com.scoto.hadila.ui.collections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoto.hadila.data.model.Collection
import com.scoto.hadila.databinding.ItemCollectionsBinding
import com.scoto.hadila.ui.common.ItemSelectListener


class CollectionsAdapter : RecyclerView.Adapter<CollectionsAdapter.MyViewHolder>() {

    private lateinit var collections: List<Collection>
    private lateinit var listener: ItemSelectListener

    inner class MyViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cvItemCollection.setOnClickListener {
                //navigate to
                listener.getItem(collections[adapterPosition])
            }

        }

        fun bind(collection: Collection) {
            binding.collection = collection
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            ItemCollectionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(collections[position])
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    fun setCollections(list: List<Collection>) {
        this.collections = list
    }

    fun setListener(listener: ItemSelectListener) {
        this.listener = listener
    }
}
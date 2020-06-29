package com.example.petslist.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerViewAdapter(private val listeners: ItemListListeners) :RecyclerView.Adapter<MyViewHolder>(){
    private val catsList:ArrayList<Dog> =ArrayList()

    private fun setData(newList : List<Dog>){
        catsList.clear()
        catsList.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item,parent,false)
        return MyViewHolder(itemView,listeners)
    }

    override fun getItemCount(): Int {
        return catsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(catsList[position])
    }

    fun updateRecyclerViewList( newList: List<Dog>){
        val taskDiffUtilCallback = DogDiffUtil(catsList, newList)
        val taskDiffResult = DiffUtil.calculateDiff(taskDiffUtilCallback)
        setData(newList)
        taskDiffResult.dispatchUpdatesTo(this)
    }
}

class MyViewHolder( private val view:View, private val listeners:ItemListListeners) : RecyclerView.ViewHolder(view){
    fun bind(dog: Dog){
        Glide.with(view.context)
            .load(dog.url)
            .placeholder(R.drawable.ic_dog_placeholder)
            .into(view.imageView_cat)
    }
}

class DogDiffUtil(private val oldList: List<Dog>, private val newList: List<Dog> ) :DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }
    override fun getOldListSize(): Int =  oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ( oldList[oldItemPosition].url == newList[newItemPosition].url)
    }
}

interface ItemListListeners {
    fun toDoLater()
}
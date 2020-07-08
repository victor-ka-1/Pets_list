package com.example.petslist.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class LikedDogsRecyclerViewAdapter(private val listeners: FavItemListListeners) :RecyclerView.Adapter<LikedDogViewHolder>(){
    private val dogsList:ArrayList<Dog> =ArrayList()

    private fun setData(newList : List<Dog>){
        dogsList.clear()
        dogsList.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedDogViewHolder {
        val itemView =LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item,parent,false)
        itemView.iv_like.setImageResource(R.drawable.icon_heart_clicked)
        return LikedDogViewHolder(itemView,listeners)
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    override fun onBindViewHolder(holder: LikedDogViewHolder, position: Int) {
        holder.bind(dogsList[position])
    }

    fun updateRecyclerViewList( newList: List<Dog>){
        val dogDiffUtilCallback = LikedDogDiffUtil(dogsList, newList)
        val dogDiffResult = DiffUtil.calculateDiff(dogDiffUtilCallback)
        setData(newList)
        dogDiffResult.dispatchUpdatesTo(this)
    }
}

class LikedDogViewHolder(private val view1:View, private val listeners:FavItemListListeners) : RecyclerView.ViewHolder(view1){
    fun bind(dog: Dog){
        Glide.with(view1.context)
            .load(dog.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_dog_placeholder)
            .into(view1.imageView_dog)

        view1.like_layout.setOnClickListener {
            listeners.unlikeClicked(dog = dog)

        }
        view1.download_layout.setOnClickListener {
            listeners.downloadClicked(dog)
        }
    }

}

class LikedDogDiffUtil(private val oldList: List<Dog>, private val newList: List<Dog> ) :DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }
    override fun getOldListSize(): Int =  oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ( oldList[oldItemPosition].url == newList[newItemPosition].url)
    }
}

interface FavItemListListeners {
    fun unlikeClicked(dog: Dog)
    fun downloadClicked(dog: Dog)
}
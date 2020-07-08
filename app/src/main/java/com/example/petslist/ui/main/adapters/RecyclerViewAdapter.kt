package com.example.petslist.ui.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import kotlinx.android.synthetic.main.footer_button.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.*


private const val VIEW_TYPE_FOOTER =1
    private const val VIEW_TYPE_ITEM = 0

class RecyclerViewAdapter(private val listeners: ItemListListeners) :RecyclerView.Adapter<DogViewHolder>(){
    private val dogsList:ArrayList<Dog> =ArrayList()

    private fun setData(newList : List<Dog>){
        dogsList.clear()
        dogsList.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        return if(viewType == VIEW_TYPE_FOOTER) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_button, parent, false)
            DogViewHolder(itemView,listeners)
      }else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
             DogViewHolder(itemView, listeners)
        }
    }

    override fun getItemCount(): Int {
        return dogsList.size+1
    }

    override fun getItemViewType(position: Int): Int {
       return if( position == dogsList.size) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }


    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        if(position == dogsList.size) {
            holder.bindFootage()
        }
        else
        holder.bind(dogsList[position])
    }

    fun updateRecyclerViewList( newList: List<Dog>){
        val dogDiffUtilCallback = DogDiffUtil(dogsList, newList)
        val dogDiffResult = DiffUtil.calculateDiff(dogDiffUtilCallback)
        setData(newList)
        dogDiffResult.dispatchUpdatesTo(this)
    }
}

class DogViewHolder(private val view1:View, private val listeners:ItemListListeners) : RecyclerView.ViewHolder(view1){
    fun bind(dog: Dog){
        Glide.with(view1.context)
            .load(dog.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_dog_placeholder)
            .into(view1.imageView_dog)

        if(dog.liked)  view1.iv_like.setImageResource(R.drawable.icon_heart_clicked)
        else view1.iv_like.setImageResource(R.drawable.icon_heart_empty)

        view1.like_layout.setOnClickListener {
            if(it.iv_like.tag == "don't liked") {
                view1.iv_like.setImageResource(R.drawable.icon_heart_clicked)
                it.iv_like.tag = "liked"
                listeners.likeClicked(dog = dog)
            }
            else{
                view1.iv_like.setImageResource(R.drawable.icon_heart_empty)
                it.iv_like.tag = "don't liked"
                listeners.unlikeClicked(dog = dog)
            }
        }
        view1.download_layout.setOnClickListener {
            listeners.downloadClicked(dog)
        }
    }
    fun bindFootage(){
        view1.loadMore_btn.setOnClickListener {
            listeners.footerBtnClicked()
        }
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
    fun likeClicked(dog: Dog)
    fun unlikeClicked(dog: Dog)
    fun downloadClicked(dog: Dog)
    fun footerBtnClicked()
}
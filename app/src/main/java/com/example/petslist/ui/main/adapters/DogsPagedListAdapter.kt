package com.example.petslist.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import com.example.petslist.ui.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class DogsPagedListAdapter( private val listeners: ItemListListeners)
    : PagedListAdapter<Dog, DogViewHolder>(
    DogDiffUtil()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
       return DogViewHolder(
           itemView,
           listeners
       )
    }
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class DogViewHolder(private val view1: View,
                    private val listeners: ItemListListeners) : RecyclerView.ViewHolder(view1){
    fun bind(dog: Dog){
        Glide.with(view1.context)
            .load(dog.url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_dog_placeholder)
            .into(view1.imageView_dog)


        if( dog.liked) {
            view1.iv_like.setImageResource(R.drawable.icon_heart_clicked)
        } else  {
            view1.iv_like.setImageResource(R.drawable.icon_heart_empty)
        }


        view1.like_layout.setOnClickListener {

            if(!dog.liked) {
                view1.iv_like.setImageResource(R.drawable.icon_heart_clicked)
                dog.liked = true
                listeners.likeClicked(dog = dog)
            }
            else{
                view1.iv_like.setImageResource(R.drawable.icon_heart_empty)
                dog.liked = false
                listeners.unlikeClicked(dog = dog)
            }
        }
        view1.download_layout.setOnClickListener {
            listeners.downloadClicked(dog)
        }
    }
}
class DogDiffUtil :DiffUtil.ItemCallback<Dog>(){

    override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem.liked == newItem.liked
                &&oldItem.url ==newItem.url
                && oldItem.id == newItem.id

    }
}

interface ItemListListeners {
    fun likeClicked(dog: Dog)
    fun unlikeClicked(dog: Dog)
    fun downloadClicked(dog: Dog)
}
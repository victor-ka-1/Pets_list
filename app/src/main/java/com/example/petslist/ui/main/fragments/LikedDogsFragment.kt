package com.example.petslist.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import com.example.petslist.ui.main.MainActivity
import com.example.petslist.ui.main.adapters.FavDogsRecyclerViewAdapter
import com.example.petslist.ui.main.adapters.FavItemListListeners
import com.example.petslist.ui.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.fragment_liked_dogs.*


class LikedDogsFragment : Fragment() {

    private val viewModel : DogsViewModel by activityViewModels()
    private lateinit var myAdapter: FavDogsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liked_dogs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupObservers()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LikedDogsFragment()
    }

    private fun setupUI(){
        val rwLayoutManager = LinearLayoutManager(activity)
        myAdapter = FavDogsRecyclerViewAdapter(object : FavItemListListeners {

            override fun unlikeClicked(dog: Dog) {
                viewModel.removeDogFromFav(dog)
            }

            override fun downloadClicked(dog: Dog) {
                if (!(activity as MainActivity).verifyPermissions()) {
                    return
                }else {
                    viewModel.downloadImage(dog.url)
                }
               // Toast.makeText(activity,"Загрузка", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView2.apply {
            layoutManager = rwLayoutManager
            adapter = myAdapter
        }
    }


    private fun setupObservers() {
        viewModel.favDogsLiveData.observe(viewLifecycleOwner, Observer { updatedList ->
            if(updatedList != null) {
                if (updatedList.isNotEmpty()) tv_shame.visibility = View.GONE
                else tv_shame.visibility = View.VISIBLE
                myAdapter.updateRecyclerViewList(updatedList)
            }
        })
    }

}
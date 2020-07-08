package com.example.petslist.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import com.example.petslist.ui.main.MainActivity
import com.example.petslist.ui.main.adapters.ItemListListeners
import com.example.petslist.ui.main.adapters.RecyclerViewAdapter
import com.example.petslist.ui.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_all_dogs.*


class AllDogsFragment : Fragment() {
    private val viewModel :DogsViewModel by activityViewModels()
    private lateinit var myAdapter: RecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_dogs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupObservers()


    }
    companion object {
        @JvmStatic
        fun newInstance() = AllDogsFragment()
    }

    private fun setupUI(){
        val rwLayoutManager = LinearLayoutManager(activity)
        myAdapter = RecyclerViewAdapter(object : ItemListListeners {
            override fun likeClicked(dog: Dog) {
                viewModel.addDogToLiked(dog)
                dog.liked = true
                (activity as MainActivity).bottomNavigationView.getOrCreateBadge(R.id.miLikedDogs).apply {
                    number += 1
                    isVisible = number > 0
                }
            }
            override fun unlikeClicked(dog: Dog) {
                viewModel.removeDogFromLiked(dog)
                dog.liked= false

                (activity as MainActivity).bottomNavigationView.getOrCreateBadge(R.id.miLikedDogs).apply {
                    if(number > 0) number -= 1
                    isVisible = number > 0
                }
            }
            override fun downloadClicked(dog: Dog) {
                if (!(activity as MainActivity).verifyPermissions()) {
                    return
                }else {
                    viewModel.downloadImage(dog.url)
                }
            }
            override fun footerBtnClicked() {
                viewModel.getNumberOfRandomDogs(10)
            }
        })
        recyclerView1.apply {
            layoutManager = rwLayoutManager
            adapter = myAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getAllDogsLiveData().observe(viewLifecycleOwner, Observer {list ->
            myAdapter.updateRecyclerViewList(list)
        })
        progressBar1.visibility = View.GONE
        recyclerView1.visibility = View.VISIBLE
    }
}
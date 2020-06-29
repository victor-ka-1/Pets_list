package com.example.petslist.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petslist.R
import com.example.petslist.data.api.ApiHelper
import com.example.petslist.data.api.RetrofitBuilder
import com.example.petslist.ui.MainViewModel
import com.example.petslist.ui.base.ViewModelFactory
import com.example.petslist.ui.main.adapter.ItemListListeners
import com.example.petslist.ui.main.adapter.RecyclerViewAdapter
import com.example.petslist.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var myAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getNumberOfRandomDogs(10,null).observe(this, Observer {
            it.let {resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { dogs -> myAdapter.updateRecyclerViewList(dogs) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        textView.text = it.message
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupUI() {
        val rwLayoutManager =LinearLayoutManager(this@MainActivity)
        myAdapter = RecyclerViewAdapter(object : ItemListListeners{
            override fun toDoLater() {

            }
        })
        recyclerView.apply {
            layoutManager = rwLayoutManager
            adapter = myAdapter
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(ApiHelper(RetrofitBuilder.theDogApi)))
            .get(MainViewModel::class.java)
    }
}

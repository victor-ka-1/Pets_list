package com.example.petslist.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.petslist.App
import com.example.petslist.R
import com.example.petslist.ui.main.fragments.AllDogsFragment
import com.example.petslist.ui.main.fragments.LikedDogsFragment
import com.example.petslist.ui.viewmodel.DogsViewModel
import com.example.petslist.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: DogsViewModel by viewModels{ viewModelFactory}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this@MainActivity)

        var number:Int
        viewModel.favDogsLiveData.observe(this, Observer { number=it.size })
        val allDogsFragment = AllDogsFragment.newInstance()
        val likedDogsFragment = LikedDogsFragment.newInstance()

        setCurrentFragment(allDogsFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miAllDogs -> setCurrentFragment(allDogsFragment)
                R.id.miLikedDogs -> setCurrentFragment(likedDogsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment :Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

}

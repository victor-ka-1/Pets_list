package com.example.petslist.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

        //initial portion of dogs
        viewModel.getNumberOfRandomDogs(10)

        val allDogsFragment = AllDogsFragment.newInstance()
        val likedDogsFragment = LikedDogsFragment.newInstance()

        setCurrentFragment(allDogsFragment)
        likedDogsBadgeReset()
        bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.miAllDogs -> setCurrentFragment(allDogsFragment)
                    R.id.miLikedDogs -> {
                        setCurrentFragment(likedDogsFragment)
                        likedDogsBadgeReset()
                    }
                }
                true
        }
    }
    private fun likedDogsBadgeReset(){
        bottomNavigationView.getOrCreateBadge(R.id.miLikedDogs).apply {
            number =0
            isVisible = false
        }
    }

    private fun setCurrentFragment(fragment :Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    fun verifyPermissions(): Boolean {
        val permissionExternalMemory =
            ActivityCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, 1)
            return false
        }
        return true
    }

}

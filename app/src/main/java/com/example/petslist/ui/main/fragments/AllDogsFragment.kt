package com.example.petslist.ui.main.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.petslist.R
import com.example.petslist.data.model.Dog
import com.example.petslist.ui.main.adapters.ItemListListeners
import com.example.petslist.ui.main.adapters.RecyclerViewAdapter
import com.example.petslist.ui.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.fragment_all_dogs.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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
        fun newInstance() =
            AllDogsFragment()
    }

    private fun setupUI(){
        val rwLayoutManager = LinearLayoutManager(activity)
        myAdapter = RecyclerViewAdapter(object : ItemListListeners {
            override fun likeClicked(dog: Dog) {
                viewModel.addDogToFav(dog)
                dog.liked = true
            }

            override fun unlikeClicked(dog: Dog) {
                viewModel.removeDogFromFav(dog)
                dog.liked= false
            }

            override fun downloadClicked(dog: Dog) {
               downloadImage(dog.url)
              //  Toast.makeText(activity,"Загрузка",Toast.LENGTH_SHORT).show()
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
        viewModel.getNumberOfRandomDogs(10)
        viewModel.getAllDogsLiveData().observe(viewLifecycleOwner, Observer {list ->
            myAdapter.updateRecyclerViewList(list)
        })
    }


    fun downloadImage(imageURL: String) {
        if (!verifyPermissions()) {
            return
        }

        val dirPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/PetApp"
        val dir = File(dirPath)
        val fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1)
        Glide.with(this)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared( placeholder: Drawable?) {}
                override fun onLoadFailed( errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(context, "Failed to Download Image. Please try again later", Toast.LENGTH_SHORT).show()
                }
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    Toast.makeText(context, "Saving Image...", Toast.LENGTH_SHORT).show()
                    saveImage(bitmap, dir, fileName)
                }
            })
    }
    fun verifyPermissions(): Boolean {
        // This will return the current Status
        val permissionExternalMemory =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val STORAGE_PERMISSIONS =
                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(requireActivity(), STORAGE_PERMISSIONS, 1)
            return false
        }
        return true
    }
    private fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {
        var isDirCreated = false
        var fOut: OutputStream? = null

        if (!storageDir.exists()) {
            isDirCreated = storageDir.mkdir()
        }
        if (isDirCreated) {
            val imageFile = File(storageDir, imageFileName)
            try {
                 fOut  = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Error while saving image!", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }finally {
                fOut?.close()
            }
        } else {
            Toast.makeText(context, "Failed to make folder!", Toast.LENGTH_SHORT).show()
        }
    }
}
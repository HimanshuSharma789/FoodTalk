package com.example.foodtalk

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.foodtalk.databinding.ActivityNewPostBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NewPostActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNewPostBinding
    private val viewModel: NewPostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)

        dispatchTakePictureIntent()

        binding.postUploadImageLayout.setOnClickListener {
            if(binding.postTitleEditText.text.isNotBlank() && viewModel.currentPhotoPath != null)
                viewModel.uploadPost(binding.postTitleEditText.text.toString())
        }

        viewModel.uploadStatus.observe(this, {
            when(it) {
                NewPostViewModel.PostUploadStatus.LOADING -> {
                    binding.uploadProgressBar.visibility = View.VISIBLE
                    binding.postUploadImage.visibility = View.GONE
                }
                NewPostViewModel.PostUploadStatus.ERROR -> {
                    binding.uploadProgressBar.visibility = View.GONE
                    binding.postUploadImage.visibility = View.VISIBLE
                    Snackbar.make(binding.root, "Upload Error. Retry!!", Snackbar.LENGTH_SHORT).show()
                }
                NewPostViewModel.PostUploadStatus.DONE -> {
                    Snackbar.make(binding.root, "Upload Successful", Snackbar.LENGTH_SHORT).show()
//                    findNavController(R.id.nav_host_fragment).navigateUp()
                    finish()
                }
            }
        })

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            this.let { it ->
                takePictureIntent.resolveActivity(it.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also { file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.foodtalk.fileprovider",
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        resultLauncher.launch(takePictureIntent)
                    }
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            viewModel.currentPhotoPath = absolutePath
        }
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Glide.with(this).load(viewModel.currentPhotoPath).into(binding.postImageView)
            } else {
//                findNavController(R.id.nav_host_fragment).navigateUp()
                finish()
            }
        }

}
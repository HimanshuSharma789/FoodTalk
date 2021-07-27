package com.example.foodtalk

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

private var storageRef: StorageReference = Firebase.storage.reference.child("image")

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

@BindingAdapter("foodBlurPostImage")
fun setFoodBlurPostImage(imageView: ImageView, imageUri: String?) {
    imageUri?.let {
        GlideApp.with(imageView.context).load(storageRef.child(it)).into(imageView)
    }
}

@BindingAdapter("foodPostImage")
fun setFoodPostImage(imageView: ImageView, imageUri: String?) {
    imageUri?.let {
        GlideApp.with(imageView.context).load(storageRef.child(it)).into(imageView)
    }
}

@BindingAdapter("foodPostTitle")
fun TextView.setFoodPostTitle(imageTitle: String) {
    text = imageTitle
}